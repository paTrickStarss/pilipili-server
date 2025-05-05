/*
 * Copyright (c) 2025. Bubble
 */

package com.bubble.pilipili.mq.consumer;

import com.bubble.pilipili.common.util.function.TriConsumer;
import com.bubble.pilipili.common.pojo.CommentStats;
import com.bubble.pilipili.mq.entity.CommentStatsMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Function;

/**
 * @author Bubble
 * @date 2025.03.14 19:17
 */
@Slf4j
@Service
@RabbitListener(queues = "queue.pilipili.stats.comment")
public class CommentStatsConsumer extends BaseStatsConsumer<CommentStatsMessage, CommentStats>{


    /**
     * 消息id getter函数
     *
     * @return
     */
    @Override
    protected Function<CommentStatsMessage, Integer> getMessageIdGetter() {
        return CommentStatsMessage::getCid;
    }

    /**
     * 统计数据实体类id getter函数
     *
     * @return
     */
    @Override
    protected Function<CommentStats, Integer> getStatsEntityIdGetter() {
        return CommentStats::getCid;
    }

    /**
     * 聚合统计消费方法
     *
     * @return (key, list, statsResultMap) -> {}
     */
    @Override
    protected TriConsumer<Integer, List<CommentStatsMessage>, Map<Integer, CommentStatsMessage>> getCollectConsumer() {
        return (key, list, statsResultMap) -> {
            AtomicLong favorCount = new AtomicLong(0);
            AtomicLong dewCount = new AtomicLong(0);
            list.forEach(stats -> {
                favorCount.addAndGet(stats.getFavorCount());
                dewCount.addAndGet(stats.getDewCount());
            });
            statsResultMap.put(key, new CommentStatsMessage(key, favorCount.get(), dewCount.get()));
        };
    }

    /**
     * 目标消息类
     *
     * @return
     */
    @Override
    protected Class<CommentStatsMessage> getMessageBodyClz() {
        return CommentStatsMessage.class;
    }

    /**
     * 目标统计数据实体类
     *
     * @return
     */
    @Override
    protected Class<CommentStats> getStatsEntityClz() {
        return CommentStats.class;
    }



}
