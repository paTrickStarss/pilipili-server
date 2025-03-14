/*
 * Copyright (c) 2025. Bubble
 */

package com.bubble.pilipili.feign.config;

import okhttp3.OkHttpClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Bubble
 * @date 2025.03.14 15:28
 */
@Configuration
public class FeignConfig {

    /**
     * 配置okHttpClient，记得配置{@code feign.okhttp.enabled = true}，
     * 否则无法通过Spring-Cloud-OpenFeign的LoadBalancerFeignClient解析服务host
     * @return
     */
    @Bean
    public OkHttpClient client() {
        return new OkHttpClient();
    }

}
