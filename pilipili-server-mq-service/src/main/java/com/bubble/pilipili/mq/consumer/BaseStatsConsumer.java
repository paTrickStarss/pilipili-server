/*
 * Copyright (c) 2025. Bubble
 */

package com.bubble.pilipili.mq.consumer;

import com.bubble.pilipili.mq.constant.QueueEnum;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.stereotype.Service;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * @author Bubble
 * @date 2025.03.14 19:06
 */
@Service
public abstract class BaseStatsConsumer {

    private final static int BATCH_SIZE = 10;
    private final BlockingQueue<String> messageQueue = new LinkedBlockingQueue<>();

    @RabbitHandler
    public void receiveMessage(String message) {
        messageQueue.add(message);
        checkMessageQueue();
    }

    protected void checkMessageQueue() {
        if (messageQueue.size() >= BATCH_SIZE) {
            handleMessages();
            messageQueue.clear();
        }
    }

    protected void handleMessages() {

    }
}
