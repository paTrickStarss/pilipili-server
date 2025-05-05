/*
 * Copyright (c) 2025. Bubble
 */

package com.bubble.pilipili.mq.consumer;

import com.bubble.pilipili.common.util.function.TriConsumer;
import com.bubble.pilipili.common.pojo.VideoStats;
import com.bubble.pilipili.mq.entity.VideoStatsMessage;
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
@RabbitListener(queues = "queue.pilipili.stats.video")
public class VideoStatsConsumer extends BaseStatsConsumer<VideoStatsMessage, VideoStats> {

    /**
     * 消息id getter函数
     *
     * @return
     */
    @Override
    protected Function<VideoStatsMessage, Integer> getMessageIdGetter() {
        return VideoStatsMessage::getVid;
    }

    /**
     * 统计数据实体类id getter函数
     *
     * @return
     */
    @Override
    protected Function<VideoStats, Integer> getStatsEntityIdGetter() {
        return VideoStats::getVid;
    }


    /**
     * 聚合统计消费方法
     *
     * @return (key, list, statsResultMap) -> {}
     */
    @Override
    protected TriConsumer<Integer, List<VideoStatsMessage>, Map<Integer, VideoStatsMessage>> getCollectConsumer() {
        return (key, list, statsResultMap) -> {
            AtomicLong viewCount = new AtomicLong(0);
            AtomicLong danmakuCount = new AtomicLong(0);
            AtomicLong commentCount = new AtomicLong(0);
            AtomicLong favorCount = new AtomicLong(0);
            AtomicLong coinCount = new AtomicLong(0);
            AtomicLong collectCount = new AtomicLong(0);
            AtomicLong repostCount = new AtomicLong(0);
            AtomicLong dewCount = new AtomicLong(0);
            list.forEach(stats -> {
                viewCount.addAndGet(stats.getViewCount());
                danmakuCount.addAndGet(stats.getDanmakuCount());
                commentCount.addAndGet(stats.getCommentCount());
                favorCount.addAndGet(stats.getFavorCount());
                coinCount.addAndGet(stats.getCoinCount());
                collectCount.addAndGet(stats.getCollectCount());
                repostCount.addAndGet(stats.getRepostCount());
                dewCount.addAndGet(stats.getDewCount());
            });
            statsResultMap.put(key,
                    new VideoStatsMessage(
                            key,
                            viewCount.get(),
                            danmakuCount.get(),
                            commentCount.get(),
                            favorCount.get(),
                            coinCount.get(),
                            collectCount.get(),
                            repostCount.get(),
                            dewCount.get()
                    )
            );
        };
    }

    /**
     * 目标消息类
     *
     * @return
     */
    @Override
    protected Class<VideoStatsMessage> getMessageBodyClz() {
        return VideoStatsMessage.class;
    }

    /**
     * 目标统计数据实体类
     *
     * @return
     */
    @Override
    protected Class<VideoStats> getStatsEntityClz() {
        return VideoStats.class;
    }

}
