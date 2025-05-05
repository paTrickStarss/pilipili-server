/*
 * Copyright (c) 2025. Bubble
 */

package com.bubble.pilipili.mq.consumer;

import com.bubble.pilipili.common.util.function.TriConsumer;
import com.bubble.pilipili.common.pojo.DanmakuStats;
import com.bubble.pilipili.mq.entity.DanmakuStatsMessage;
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
@RabbitListener(queues = "queue.pilipili.stats.danmaku")
public class DanmakuStatsConsumer extends BaseStatsConsumer<DanmakuStatsMessage, DanmakuStats> {
    /**
     * 消息id getter函数
     *
     * @return
     */
    @Override
    protected Function<DanmakuStatsMessage, Integer> getMessageIdGetter() {
        return DanmakuStatsMessage::getDanmakuId;
    }

    /**
     * 统计数据实体类id getter函数
     *
     * @return
     */
    @Override
    protected Function<DanmakuStats, Integer> getStatsEntityIdGetter() {
        return DanmakuStats::getDanmakuId;
    }

    /**
     * 聚合统计消费方法
     *
     * @return (key, list, statsResultMap) -> {}
     */
    @Override
    protected TriConsumer<Integer, List<DanmakuStatsMessage>, Map<Integer, DanmakuStatsMessage>> getCollectConsumer() {
        return (key, list, statsResultMap) -> {
            AtomicLong favorCount = new AtomicLong(0);
            AtomicLong dewCount = new AtomicLong(0);
            list.forEach(stats -> {
                favorCount.addAndGet(stats.getFavorCount());
                dewCount.addAndGet(stats.getDewCount());
            });
            statsResultMap.put(key, new DanmakuStatsMessage(key, favorCount.get(), dewCount.get()));
        };
    }

    /**
     * 目标消息类
     *
     * @return
     */
    @Override
    protected Class<DanmakuStatsMessage> getMessageBodyClz() {
        return DanmakuStatsMessage.class;
    }

    /**
     * 目标统计数据实体类
     *
     * @return
     */
    @Override
    protected Class<DanmakuStats> getStatsEntityClz() {
        return DanmakuStats.class;
    }

}
