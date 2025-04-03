/*
 * Copyright (c) 2025. Bubble
 */

package com.bubble.pilipili.mq.util;

import com.bubble.pilipili.common.exception.MQConsumerException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;

/**
 * @author Bubble
 * @date 2025.03.26 17:15
 */
@Component
@Slf4j
public class MessageHelper {

    /**
     * 记录重试次数的消息头
     */
    public static final String HEADER_RETRY_COUNT = "x-retry-count";
    /**
     * 最大重试次数
     */
    public static final int MAX_RETRY_COUNT = 3;
    /**
     * 初始重试间隔
     */
    public static final int INIT_DELAY_MS = 3000;
    /**
     * 重试间隔倍数
     */
    public static final int RETRY_MULTIPLIER = 3;

    /**
     * 消息体类对象缓存Map
     */
    private static final HashMap<String, Class<?>> clzCacheMap = new HashMap<>();


    @Autowired
    private ObjectMapper objectMapper;

    /**
     * 获取消息体
     * @param msg
     * @return
     */
    public Object getMessageBody(Message msg) {
        String bodyStr = new String(msg.getBody());
        String typeId = msg.getMessageProperties().getHeader("__TypeId__");
        Class<?> bodyClz = getClz(typeId);
        try {
            return objectMapper.readValue(bodyStr, bodyClz);
        } catch (JsonProcessingException e) {
            log.error("Message body JSON processing error: {}", bodyStr);
            throw new MQConsumerException(e.getMessage());
        }
    }

    public Class<?> getClz(String typeId) {
        return clzCacheMap.computeIfAbsent(
                typeId,
                type -> {
                    try {
                        return Class.forName(typeId);
                    } catch (ClassNotFoundException e) {
                        log.error("Message body class not found: {}", typeId);
                        throw new MQConsumerException(e.getMessage());
                    }
                }
        );
    }
}
