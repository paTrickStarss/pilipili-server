/*
 * Copyright (c) 2025. Bubble
 */

package com.bubble.pilipili.mq;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author Bubble
 * @date 2025.03.14 17:52
 */
@SpringBootApplication
@ComponentScan(basePackages = {"com.bubble.pilipili.common", "com.bubble.pilipili.feign", "com.bubble.pilipili.mq"})
@EnableFeignClients(basePackages = {"com.bubble.pilipili.feign.api"})
public class MQApplication {

    public static void main(String[] args) {
        SpringApplication.run(MQApplication.class, args);
    }
}
