/*
 * Copyright (c) 2025. Bubble
 */

package com.bubble.pilipili.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * @author Bubble
 * @date 2025.03.09 22:39
 */
@Configuration
@EnableAsync
public class ThreadPoolConfig {

    @Bean("bubbleThreadPool")
    public ThreadPoolTaskExecutor bubbleThreadPool() {
        int availableProcessors = Runtime.getRuntime().availableProcessors();

        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        // 核心线程数
        executor.setCorePoolSize(availableProcessors + 1);
        // 最大线程数
        executor.setMaxPoolSize(availableProcessors * 2 + 1);
        // 阻塞队列容量
        executor.setQueueCapacity(50);
        // 空闲线程存活时间
        executor.setKeepAliveSeconds(60);
        // 线程名称前缀
        executor.setThreadNamePrefix("bubble-thread-");
        executor.initialize();
        return executor;
    }

}
