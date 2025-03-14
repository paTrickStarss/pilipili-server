/*
 * Copyright (c) 2025. Bubble
 */

package com.bubble.pilipili.interact;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author Bubble
 * @date 2025.02.28 20:25
 */
@SpringBootApplication
@ComponentScan(basePackages = {"com.bubble.pilipili.common", "com.bubble.pilipili.feign", "com.bubble.pilipili.interact"})
@EnableFeignClients(basePackages = {"com.bubble.pilipili.feign.api"})
public class InteractApplication {

    public static void main(String[] args) {
        SpringApplication.run(InteractApplication.class, args);
    }
}
