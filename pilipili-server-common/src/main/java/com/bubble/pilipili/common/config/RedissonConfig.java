/*
 * Copyright (c) 2025. Bubble
 */

package com.bubble.pilipili.common.config;

import org.redisson.spring.starter.RedissonAutoConfigurationCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Redisson自定义配置
 * @author Bubble
 * @date 2025.05.05 22:28
 */
@Configuration
public class RedissonConfig {

    @Bean
    public RedissonAutoConfigurationCustomizer customizer() {
        return config ->
                config
                        .useSingleServer()
                        .setConnectionMinimumIdleSize(10)
                        .setConnectionPoolSize(32);
    }
}
