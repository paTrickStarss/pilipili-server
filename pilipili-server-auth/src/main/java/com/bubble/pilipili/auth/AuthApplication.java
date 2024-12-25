/*
 * Copyright (c) 2024. Bubble
 */

package com.bubble.pilipili.auth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @author liweixin@hcrc1.wecom.work
 * @date 2024/10/24
 */
@SpringBootApplication
@EnableDiscoveryClient
public class AuthApplication {

    public static void main(String[] args) {
        SpringApplication.run(AuthApplication.class, args);
    }
}
