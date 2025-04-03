/*
 * Copyright (c) 2025. Bubble
 */

package com.bubble.pilipili.mq.producer;

import com.bubble.pilipili.mq.constant.ExchangeEnum;
import com.bubble.pilipili.mq.constant.QueueEnum;
import com.bubble.pilipili.mq.entity.*;
import com.bubble.pilipili.mq.util.MessageHelper;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.UUID;

/**
 * @author Bubble
 * @date 2025.03.15 13:55
 */
@Service
public class MessageProducer {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    /**
     * 发送统计数据信息
     * @param statsMessage
     */
    public void sendStatsMessage(StatsMessage statsMessage) {
        Objects.requireNonNull(statsMessage);

        if (statsMessage instanceof CommentStatsMessage) {
            sendCommentStats((CommentStatsMessage) statsMessage);

        } else if (statsMessage instanceof DynamicStatsMessage) {
            sendDynamicStats((DynamicStatsMessage) statsMessage);

        } else if (statsMessage instanceof DanmakuStatsMessage) {
            sendDanmakuStats((DanmakuStatsMessage) statsMessage);

        } else if (statsMessage instanceof VideoStatsMessage) {
            sendVideoStats(((VideoStatsMessage) statsMessage));
        }
    }

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
     * 发送视频
     * @param message
     */
    public void sendVideoInfo(VideoInfoMessage message) {
        rabbitTemplate.convertAndSend(
                ExchangeEnum.EXCHANGE_INFO.getName(),
                QueueEnum.QUEUE_INFO_VIDEO.getRoutingKey(),
                message,
                generateCorrelationData()
        );
    }

    /**
     * 发送视频信息更新消息（延迟一定时间后消费，用于重试）
     * @param message
     * @param delay
     * @param retryCount
     */
    public void sendVideoInfo(VideoInfoMessage message, Integer delay, Integer retryCount) {
        rabbitTemplate.convertAndSend(
                ExchangeEnum.EXCHANGE_DELAY.getName(),
                QueueEnum.QUEUE_DELAY.getRoutingKey(),
                message,
                a -> {
                    a.getMessageProperties().setDelay(delay);
                    a.getMessageProperties().setHeader(MessageHelper.HEADER_RETRY_COUNT, retryCount);
                    return a;
                },
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
