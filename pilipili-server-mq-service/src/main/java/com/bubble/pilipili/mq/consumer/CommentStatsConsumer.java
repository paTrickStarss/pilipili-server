/*
 * Copyright (c) 2025. Bubble
 */

package com.bubble.pilipili.mq.consumer;

import com.bubble.pilipili.feign.api.StatsFeignAPI;
import com.bubble.pilipili.feign.pojo.entity.CommentStats;
import com.bubble.pilipili.mq.entity.CommentStatsMessage;
import com.bubble.pilipili.mq.util.ConsumerAction;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Bubble
 * @date 2025.03.14 19:17
 */
@Slf4j
@Service
@RabbitListener(queues = "queue.pilipili.stats.comment")
public class CommentStatsConsumer extends BaseStatsConsumer<CommentStatsMessage>{

    @Autowired
    private StatsFeignAPI statsFeignAPI;

    /**
     * 目标消息类
     *
     * @return
     */
    @Override
    protected Class<CommentStatsMessage> getBodyClz() {
        return CommentStatsMessage.class;
    }

    /**
     * 处理消息
     *
     * @param statsBatchList
     */
    @Override
    protected void handleStatsMessages(List<CommentStatsMessage> statsBatchList) {
//        log.debug("statsBatchList size:{}", statsBatchList.size());
//        Map<Integer, CommentStatsMessage> statsResultMap = new HashMap<>();
//        Map<Integer, List<CommentStatsMessage>> collect = statsBatchList.stream()
//                .collect(Collectors.groupingBy(CommentStatsMessage::getCid));
//
//        // 聚合统计
//        collect.forEach((key, value) -> {
//                    AtomicLong favorCount = new AtomicLong(0);
//                    AtomicLong dewCount = new AtomicLong(0);
//                    value.forEach(stats -> {
//                        favorCount.addAndGet(stats.getFavorCount());
//                        dewCount.addAndGet(stats.getDewCount());
//                    });
//                    statsResultMap.put(key, new CommentStatsMessage(key, favorCount.get(), dewCount.get()));
//                });
        /*
        Map<Integer, CommentStatsMessage> statsResultMap = statsBatchList.stream()
                .collect(Collectors.groupingBy(
                        CommentStatsMessage::getCid,
                        Collectors.collectingAndThen(
                                Collectors.reducing(
                                        new CommentStatsMessage(0, 0L, 0L), // 初始值
                                        message -> new CommentStatsMessage(
                                                message.getCid(),
                                                message.getFavorCount(),
                                                message.getDewCount()
                                        ),
                                        (left, right) -> new CommentStatsMessage(
                                                left.getCid(),
                                                left.getFavorCount() + right.getFavorCount(),
                                                left.getDewCount() + right.getDewCount()
                                        )
                                ),
                                reduced -> reduced // 提取最终结果
                        )
                ));

        List<CommentStatsMessage> statsList = new ArrayList<>(statsResultMap.values());

        statsList.forEach(System.out::println);
        */
        ConsumerAction.consumeStatsMessage(
                CommentStatsMessage.class,
                CommentStats.class,
                statsBatchList,
                CommentStatsMessage::getCid,
                csm -> {
                    csm.setCid(0);
                    csm.setFavorCount(0L);
                    csm.setDewCount(0L);
                },
                csm -> new CommentStatsMessage(
                        csm.getCid(),
                        csm.getFavorCount(),
                        csm.getDewCount()
                ),
                (left, right) -> new CommentStatsMessage(
                        left.getCid(),
                        left.getFavorCount() + right.getFavorCount(),
                        left.getDewCount() + right.getDewCount()
                ),
                statsFeignAPI
        );
    }


}
