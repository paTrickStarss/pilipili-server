package com.bubble.pilipili.auth.config;

import com.bubble.pilipili.common.config.RedisConfig;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * 使用common包的redis配置
 * @author Bubble
 * @date 2024.12.28 20:50
 */
@Configuration
@Import(RedisConfig.class)
public class AuthRedisConfig {
}
