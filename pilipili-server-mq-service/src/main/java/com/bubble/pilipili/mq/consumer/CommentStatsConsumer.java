/*
 * Copyright (c) 2025. Bubble
 */

package com.bubble.pilipili.mq.consumer;

import org.springframework.amqp.rabbit.annotation.RabbitListener;

/**
 * @author Bubble
 * @date 2025.03.14 19:17
 */

@RabbitListener(queues = "queue.pilipili.stats.comment")
public class CommentStatsConsumer extends BaseStatsConsumer{

    @Override
    protected void handleMessages() {


    }
}
