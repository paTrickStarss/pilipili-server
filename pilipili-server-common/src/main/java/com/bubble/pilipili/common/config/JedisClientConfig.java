/*
 * Copyright (c) 2024-2025. Bubble
 */

package com.bubble.pilipili.common.config;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.data.redis.connection.jedis.JedisClientConfiguration;
import redis.clients.jedis.JedisPoolConfig;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLParameters;
import javax.net.ssl.SSLSocketFactory;
import java.time.Duration;
import java.util.Optional;

/**
 * @author liweixin@hcrc1.wecom.work
 * @date 2024/12/30
 */
public class JedisClientConfig implements JedisClientConfiguration {

    private final JedisPoolConfig jedisPoolConfig;
    private final String clientName = "jedisClient";

    public JedisClientConfig(JedisPoolConfig jedisPoolConfig) {
        this.jedisPoolConfig = jedisPoolConfig;
    }

    @Override
    public boolean isUseSsl() {
        return false;
    }

    @Override
    public Optional<SSLSocketFactory> getSslSocketFactory() {
        return Optional.empty();
    }

    @Override
    public Optional<SSLParameters> getSslParameters() {
        return Optional.empty();
    }

    @Override
    public Optional<HostnameVerifier> getHostnameVerifier() {
        return Optional.empty();
    }

    @Override
    public boolean isUsePooling() {
        return true;
    }

    @Override
    public Optional<GenericObjectPoolConfig> getPoolConfig() {
        return Optional.of(jedisPoolConfig);
    }

    @Override
    public Optional<String> getClientName() {
        return Optional.of(clientName);
    }

    @Override
    public Duration getConnectTimeout() {
        return Duration.ofSeconds(30);
    }

    @Override
    public Duration getReadTimeout() {
        return Duration.ofSeconds(30);
    }
}
