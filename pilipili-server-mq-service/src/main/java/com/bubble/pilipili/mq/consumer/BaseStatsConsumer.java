/*
 * Copyright (c) 2025. Bubble
 */

package com.bubble.pilipili.mq.consumer;

import com.bubble.pilipili.mq.entity.StatsMessage;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * @author Bubble
 * @date 2025.03.14 19:06
 */
@Slf4j
@Service
public abstract class BaseStatsConsumer<T extends StatsMessage> {

    /**
     * 消息处理批次大小
     */
    protected final static int BATCH_SIZE = 10;
    private final HashMap<String, Class<?>> clzCacheMap = new HashMap<>();
    private final BlockingQueue<T> statsQueue = new LinkedBlockingQueue<>(BATCH_SIZE+10);

    @Autowired
    private ObjectMapper objectMapper;

    /**
     * 目标消息类
     * @return
     */
    protected abstract Class<T> getBodyClz();

    /**
     * 处理消息
     */
    protected abstract void handleStatsMessages(List<T> statsBatchList);

    @RabbitHandler
    public void defaultConsumer(Object message) {
        Message msg = (Message) message;
        Object messageBody = getMessageBody(msg);
        if (getBodyClz().isInstance(messageBody)) {
            // 确认是目标消息类型，下发处理
//            log.debug("Message body matched, launch handler...");
            T body = getBodyClz().cast(messageBody);
            receiveStatsMessage(body);
        }
    }

    private void receiveStatsMessage(T message) {
        try {
//            statsQueue.add(message);
            statsQueue.put(message);
        } catch (Exception e) {
            log.warn("statsQueue插入异常: {}", e.getMessage());
        }
        checkStatsQueue();
    }

    /**
     * 检查队列
     */
    private void checkStatsQueue() {
//        log.debug("Current queue size: {}", statsQueue.size());
        if (statsQueue.size() >= BATCH_SIZE) {
            List<T> statsBatchList = new ArrayList<>();
            statsQueue.drainTo(statsBatchList);
            handleStatsMessages(statsBatchList);
//            statsQueue.clear();
        }
    }



    /**
     * 获取消息体
     * @param msg
     * @return
     */
    private Object getMessageBody(Message msg) {
        String bodyStr = new String(msg.getBody());
        String typeId = msg.getMessageProperties().getHeader("__TypeId__");
        Class<?> bodyClz = getClz(typeId);
        try {
            return objectMapper.readValue(bodyStr, bodyClz);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private Class<?> getClz(String typeId) {
        return clzCacheMap.computeIfAbsent(
                typeId,
                type -> {
                    try {
                        return Class.forName(typeId);
                    } catch (ClassNotFoundException e) {
                        throw new RuntimeException(e);
                    }
                }
        );
    }
}
