/*
 * Copyright (c) 2025. Bubble
 */

package com.bubble.pilipili.mq.producer;

import com.bubble.pilipili.mq.constant.ExchangeEnum;
import com.bubble.pilipili.mq.constant.QueueEnum;
import com.bubble.pilipili.mq.entity.CommentStatsMessage;
import com.bubble.pilipili.mq.entity.DanmakuStatsMessage;
import com.bubble.pilipili.mq.entity.DynamicStatsMessage;
import com.bubble.pilipili.mq.entity.VideoStatsMessage;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * @author Bubble
 * @date 2025.03.15 13:55
 */
@Service
public class StatsMessageProducer {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    /**
     * 发送评论统计数据消息
     * @param message
     */
    public void sendCommentStats(CommentStatsMessage message) {
        rabbitTemplate.convertAndSend(
                ExchangeEnum.EXCHANGE_STATS.getName(),
                QueueEnum.QUEUE_STATS_COMMENT.getRoutingKey(),
                message,
                generateCorrelationData()
        );
    }
    /**
     * 发送动态统计数据消息
     * @param message
     */
    public void sendDynamicStats(DynamicStatsMessage message) {
        rabbitTemplate.convertAndSend(
                ExchangeEnum.EXCHANGE_STATS.getName(),
                QueueEnum.QUEUE_STATS_DYNAMIC.getRoutingKey(),
                message,
                generateCorrelationData()
        );
    }
    /**
     * 发送弹幕统计数据消息
     * @param message
     */
    public void sendDanmakuStats(DanmakuStatsMessage message) {
        rabbitTemplate.convertAndSend(
                ExchangeEnum.EXCHANGE_STATS.getName(),
                QueueEnum.QUEUE_STATS_DANMAKU.getRoutingKey(),
                message,
                generateCorrelationData()
        );
    }
    /**
     * 发送视频统计数据消息
     * @param message
     */
    public void sendVideoStats(VideoStatsMessage message) {
        rabbitTemplate.convertAndSend(
                ExchangeEnum.EXCHANGE_STATS.getName(),
                QueueEnum.QUEUE_STATS_VIDEO.getRoutingKey(),
                message,
                generateCorrelationData()
        );
    }

    /**
     * 生成correlationData，以UUID作为id
     * @return
     */
    private CorrelationData generateCorrelationData() {
        return new CorrelationData(UUID.randomUUID().toString());
    }
}
