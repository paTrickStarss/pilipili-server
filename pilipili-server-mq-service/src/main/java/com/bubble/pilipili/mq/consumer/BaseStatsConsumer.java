/*
 * Copyright (c) 2025. Bubble
 */

package com.bubble.pilipili.mq.consumer;

import com.bubble.pilipili.common.http.SimpleResponse;
import com.bubble.pilipili.common.pojo.StatsEntity;
import com.bubble.pilipili.common.pojo.converter.BaseConverter;
import com.bubble.pilipili.common.util.function.TriConsumer;
import com.bubble.pilipili.feign.api.StatsFeignAPI;
import com.bubble.pilipili.feign.pojo.entity.CommentStats;
import com.bubble.pilipili.feign.pojo.entity.DanmakuStats;
import com.bubble.pilipili.feign.pojo.entity.DynamicStats;
import com.bubble.pilipili.feign.pojo.entity.VideoStats;
import com.bubble.pilipili.mq.entity.StatsMessage;
import com.bubble.pilipili.mq.producer.MessageProducer;
import com.bubble.pilipili.mq.util.MessageHelper;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 统计数据消费者抽象类
 * @author Bubble
 * @date 2025.03.14 19:06
 */
@Slf4j
@Service
public abstract class BaseStatsConsumer<T extends StatsMessage, S extends StatsEntity> {

    @Autowired
    protected StatsFeignAPI statsFeignAPI;
    @Autowired
    protected MessageProducer messageProducer;

    @Autowired
    private MessageHelper messageHelper;

    /**
     * 消息处理批次大小
     */
    private final static int BATCH_SIZE = 5;


    /**
     * 消息批处理暂存队列
     */
    private final BlockingQueue<T> statsQueue = new LinkedBlockingQueue<>((int) (BATCH_SIZE * 1.5));

    /**
     * 消息id getter函数
     * @return
     */
    protected abstract Function<T, Integer> getMessageIdGetter();

    /**
     * 统计数据实体类id getter函数
     * @return
     */
    protected abstract Function<S, Integer> getStatsEntityIdGetter();

    /**
     * 聚合统计消费方法
     * @return (key, list, statsResultMap) -> {}
     */
    protected abstract TriConsumer<Integer, List<T>, Map<Integer, T>> getCollectConsumer();

    /**
     * 目标消息类
     * @return
     */
    protected abstract Class<T> getMessageBodyClz();
    /**
     * 目标统计数据实体类
     * @return
     */
    protected abstract Class<S> getStatsEntityClz();


    @RabbitHandler
    public void defaultConsumer(Object message, Channel channel) {
        Message msg = (Message) message;
        long deliveryTag = msg.getMessageProperties().getDeliveryTag();
        Object messageBody = messageHelper.getMessageBody(msg);
        if (getMessageBodyClz().isInstance(messageBody)) {
            // 确认是目标消息类型，下发处理
//            log.debug("Message body matched, launch handler...");
            T body = getMessageBodyClz().cast(messageBody);
            receiveStatsMessage(body);
        } else {
            log.warn("不支持的消息类型: {}", msg);
        }
        try {
            channel.basicAck(deliveryTag, false);
        } catch (IOException e) {
            log.error("消息确认异常");
            throw new RuntimeException(e);
        }
    }

    /**
     * 定时任务固定时间间隔检查一下暂存队列，若有消息则直接开启一次批处理，即使未满批处理数量
     */
    @Scheduled(fixedRate = 1, timeUnit = TimeUnit.MINUTES)
    public void scheduleConsumer() {
        log.debug("###[{}]scheduled: Current queue size: {}",
                getClass().getName(), statsQueue.size());
        if (!statsQueue.isEmpty()) {
            launchBatchHandle();
        }
    }

    private void receiveStatsMessage(T message) {
        try {
//            statsQueue.offer(message);  // 非阻塞方法，队满时返回false，可配置最大等待时间（阻塞等待一段时间）
//            statsQueue.add(message);  // 非阻塞方法，队满时抛出异常IllegalStateException
            statsQueue.put(message);  // 阻塞方法，队满时等待
        } catch (Exception e) {
            log.warn("statsQueue插入异常: {}", e.getMessage());
        }
        checkStatsQueue();
    }

    /**
     * 检查队列
     */
    private void checkStatsQueue() {
        log.debug("Current queue size: {}", statsQueue.size());
        if (statsQueue.size() >= BATCH_SIZE) {
            launchBatchHandle();
//            statsQueue.clear();
        }
    }

    /**
     * 启动一次消息批处理
     */
    private void launchBatchHandle() {
        List<T> statsBatchList = new ArrayList<>();
        statsQueue.drainTo(statsBatchList);
        handleStatsMessages(statsBatchList);
    }

    protected void failFallback(StatsMessage statsMessage, SimpleResponse<String> response) {
        log.warn("Save comment stats error, send it back to MQ. \nstats msg: {}\nFeignAPI response msg: {}",
                statsMessage, response.getMsg());
    }

    /**
     * 批量处理统计数据消息
     * @param statsBatchList
     */
    protected void handleStatsMessages(List<T> statsBatchList) {
        log.debug("statsBatchList size:{}", statsBatchList.size());
        Map<Integer, T> statsResultMap = new HashMap<>();
        Map<Integer, List<T>> collect = statsBatchList.stream()
                .collect(Collectors.groupingBy(getMessageIdGetter()));

        // 聚合统计
        collect.forEach((key, value) ->
                getCollectConsumer().accept(key, value, statsResultMap));

        List<S> statsList =
                BaseConverter.getInstance()
                        .copyFieldValueList(new ArrayList<>(statsResultMap.values()), getStatsEntityClz());

        statsList.forEach(stats -> {
            log.debug("### Apply FeignAPI for save stats: {}", stats);
            SimpleResponse<String> response = saveStatsByFeignAPI(stats);
            if (!response.isSuccess()) {
                // 保存失败，放回MQ
                T message = statsResultMap.get(getStatsEntityIdGetter().apply(stats));
                failFallback(message, response);
                messageProducer.sendStatsMessage(message);
            }
        });
    }

    /**
     * Feign远程调用保存统计数据
     * @param stats
     * @return
     */
    private SimpleResponse<String> saveStatsByFeignAPI(S stats) {
        if (stats instanceof CommentStats) {
            return statsFeignAPI.saveCommentStats((CommentStats) stats);
        } else if (stats instanceof DynamicStats) {
            return statsFeignAPI.saveDynamicStats((DynamicStats) stats);
        } else if (stats instanceof DanmakuStats) {
            return statsFeignAPI.saveDanmakuStats((DanmakuStats) stats);
        } else if (stats instanceof VideoStats) {
            return statsFeignAPI.saveVideoStats((VideoStats) stats);
        }
        // todo：用户统计数据保存

        log.error("Stats not supported: {}", stats);
        return SimpleResponse.failed("Stats type not supported");
    }





}
