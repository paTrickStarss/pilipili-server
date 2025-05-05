/*
 * Copyright (c) 2025. Bubble
 */

package com.bubble.pilipili.mq.consumer;

import com.bubble.pilipili.common.util.function.TriConsumer;
import com.bubble.pilipili.common.pojo.DynamicStats;
import com.bubble.pilipili.mq.entity.DynamicStatsMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Function;

/**
 * @author Bubble
 * @date 2025.03.15 19:37
 */
@Slf4j
@Service
@RabbitListener(queues = "queue.pilipili.stats.dynamic")
public class DynamicStatsConsumer extends BaseStatsConsumer<DynamicStatsMessage, DynamicStats> {
    /**
     * 消息id getter函数
     *
     * @return
     */
    @Override
    protected Function<DynamicStatsMessage, Integer> getMessageIdGetter() {
        return DynamicStatsMessage::getDid;
    }

    /**
     * 统计数据实体类id getter函数
     *
     * @return
     */
    @Override
    protected Function<DynamicStats, Integer> getStatsEntityIdGetter() {
        return DynamicStats::getDid;
    }

    /**
     * 聚合统计消费方法
     *
     * @return (key, list, statsResultMap) -> {}
     */
    @Override
    protected TriConsumer<Integer, List<DynamicStatsMessage>, Map<Integer, DynamicStatsMessage>> getCollectConsumer() {
        return (key, list, statsResultMap) -> {
            AtomicLong favorCount = new AtomicLong(0);
            AtomicLong commentCount = new AtomicLong(0);
            AtomicLong repostCount = new AtomicLong(0);
            list.forEach(stats -> {
                favorCount.addAndGet(stats.getFavorCount());
                commentCount.addAndGet(stats.getCommentCount());
                repostCount.addAndGet(stats.getRepostCount());
            });
            statsResultMap.put(key,
                    new DynamicStatsMessage(
                            key, favorCount.get(), commentCount.get(), repostCount.get())
            );
        };
    }

    /**
     * 目标消息类
     *
     * @return
     */
    @Override
    protected Class<DynamicStatsMessage> getMessageBodyClz() {
        return DynamicStatsMessage.class;
    }

    /**
     * 目标统计数据实体类
     *
     * @return
     */
    @Override
    protected Class<DynamicStats> getStatsEntityClz() {
        return DynamicStats.class;
    }

}
