/*
 * Copyright (c) 2025. Bubble
 */

package com.bubble.pilipili.mq.config;

import com.bubble.pilipili.common.exception.MQSendMessageException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Bubble
 * @date 2025.03.14 18:47
 */
@Slf4j
@Configuration
public class RabbitConfig {


    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);

        rabbitTemplate.setMessageConverter(new Jackson2JsonMessageConverter());

        rabbitTemplate.setConfirmCallback((correlationData, ack, cause) -> {
            if (ack) {
                // correlationData需要在消息发送时附带，否则这里为空
                assert correlationData != null;
//                log.info("消息发送成功: {}", correlationData.getId());
            } else {
                log.warn("消息未能成功发送到交换机，原因：{}", cause);
                throw new MQSendMessageException("消息发送失败");
            }
        });
        rabbitTemplate.setMandatory(true);
        rabbitTemplate.setReturnsCallback((returnedMessage) -> {

        });


        return rabbitTemplate;
    }
}
