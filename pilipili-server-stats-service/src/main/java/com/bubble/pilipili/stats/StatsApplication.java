/*
 * Copyright (c) 2025. Bubble
 */

package com.bubble.pilipili.stats;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author Bubble
 * @date 2025.03.15 19:44
 */
@SpringBootApplication
@EnableDiscoveryClient
@ComponentScan(basePackages = {"com.bubble.pilipili.common", "com.bubble.pilipili.feign", "com.bubble.pilipili.stats"})
@EnableFeignClients(basePackages = {"com.bubble.pilipili.feign.api"})
public class StatsApplication {

    public static void main(String[] args) {
        SpringApplication.run(StatsApplication.class, args);
    }
}
