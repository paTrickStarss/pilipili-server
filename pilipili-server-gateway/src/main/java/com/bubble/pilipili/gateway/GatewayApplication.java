/*
 * Copyright (c) 2024-2025. Bubble
 */

package com.bubble.pilipili.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients(basePackages = {"com.bubble.pilipili.gateway.controller"})
@ComponentScan(basePackages = {"com.bubble.pilipili.common", "com.bubble.pilipili.gateway"})
@ConfigurationPropertiesScan(basePackages = {"com.bubble.pilipili.common", "com.bubble.pilipili.gateway"})
public class GatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(GatewayApplication.class, args);
    }
}
