package com.bubble.pilipili.gateway.config;

import com.bubble.pilipili.common.config.RedisConfig;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @author Bubble
 * @date 2024.12.28 20:54
 */
@Configuration
@Import(RedisConfig.class)
public class GatewayRedisConfig {
}
