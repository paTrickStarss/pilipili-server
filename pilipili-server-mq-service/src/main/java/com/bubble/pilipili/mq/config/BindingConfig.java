/*
 * Copyright (c) 2025. Bubble
 */

package com.bubble.pilipili.mq.config;

import com.bubble.pilipili.mq.constant.ExchangeEnum;
import com.bubble.pilipili.mq.constant.QueueEnum;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * MQ交换机、队列及其绑定配置
 * @author Bubble
 * @date 2025.03.14 18:48
 */
@Configuration
public class BindingConfig {

    @Bean
    DirectExchange statsExchange() {
        return new DirectExchange(ExchangeEnum.EXCHANGE_STATS.getName(), true, false);
    }

    @Bean
    Queue commentStatsQueue() {
        return new Queue(QueueEnum.QUEUE_STATS_COMMENT.getName(), true, false, false);
    }
    @Bean
    Queue dynamicStatsQueue() {
        return new Queue(QueueEnum.QUEUE_STATS_DYNAMIC.getName(), true, false, false);
    }
    @Bean
    Queue danmakuStatsQueue() {
        return new Queue(QueueEnum.QUEUE_STATS_DANMAKU.getName(), true, false, false);
    }
    @Bean
    Queue videoStatsQueue() {
        return new Queue(QueueEnum.QUEUE_STATS_VIDEO.getName(), true, false, false);
    }

    @Bean
    Binding BindingCommentStatsQueue() {
        return BindingBuilder
                .bind(commentStatsQueue())
                .to(statsExchange())
                .with(QueueEnum.QUEUE_STATS_COMMENT.getRoutingKey());
    }
    @Bean
    Binding BindingDynamicStatsQueue() {
        return BindingBuilder
                .bind(dynamicStatsQueue())
                .to(statsExchange())
                .with(QueueEnum.QUEUE_STATS_DYNAMIC.getRoutingKey());
    }
    @Bean
    Binding BindingDanmakuStatsQueue() {
        return BindingBuilder
                .bind(danmakuStatsQueue())
                .to(statsExchange())
                .with(QueueEnum.QUEUE_STATS_DANMAKU.getRoutingKey());
    }
    @Bean
    Binding BindingVideoStatsQueue() {
        return BindingBuilder
                .bind(videoStatsQueue())
                .to(statsExchange())
                .with(QueueEnum.QUEUE_STATS_VIDEO.getRoutingKey());
    }
}
