/*
 * Copyright (c) 2025. Bubble
 */

package com.bubble.pilipili.oss;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * @author Bubble
 * @date 2025.03.21 13:49
 */
@SpringBootApplication
@EnableDiscoveryClient
@ComponentScan(basePackages = {"com.bubble.pilipili.common", "com.bubble.pilipili.feign", "com.bubble.pilipili.oss"})
@EnableFeignClients(basePackages = {"com.bubble.pilipili.feign.api"})
@EnableAsync
public class OSSApplication {

    public static void main(String[] args) {
        SpringApplication.run(OSSApplication.class, args);
    }
}
