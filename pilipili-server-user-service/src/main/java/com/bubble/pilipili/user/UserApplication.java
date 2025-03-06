/*
 * Copyright (c) 2024-2025. Bubble
 */

package com.bubble.pilipili.user;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author liweixin@hcrc1.wecom.work
 * @date 2024/10/14
 */
@SpringBootApplication
@EnableDiscoveryClient
@ComponentScan(basePackages = {"com.bubble.pilipili.common", "com.bubble.pilipili.user"})
public class UserApplication {

    public static void main(String[] args) {
        SpringApplication.run(UserApplication.class, args);
    }
}
