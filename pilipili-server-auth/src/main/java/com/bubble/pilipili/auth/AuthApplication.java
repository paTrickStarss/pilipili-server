/*
 * Copyright (c) 2024-2025. Bubble
 */

package com.bubble.pilipili.auth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author liweixin@hcrc1.wecom.work
 * @date 2024/10/24
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients(basePackages = {"com.bubble.pilipili.auth.controller"})
@ComponentScan(basePackages = {"com.bubble.pilipili.common", "com.bubble.pilipili.auth"})
@ConfigurationPropertiesScan(basePackages = {"com.bubble.pilipili.common", "com.bubble.pilipili.auth"})
public class AuthApplication {

    public static void main(String[] args) {
        SpringApplication.run(AuthApplication.class, args);
    }
}
