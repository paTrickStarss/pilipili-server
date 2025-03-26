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
