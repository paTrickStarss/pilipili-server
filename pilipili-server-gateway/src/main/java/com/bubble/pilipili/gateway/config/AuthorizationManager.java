/*
 * Copyright (c) 2024-2025. Bubble
 */

package com.bubble.pilipili.gateway.config;

import com.bubble.pilipili.common.constant.RedisKey;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.ReactiveAuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.server.authorization.AuthorizationContext;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 资源鉴权管理器
 * @author liweixin@hcrc1.wecom.work
 * @date 2024/10/24
 */
@Slf4j
@Component
public class AuthorizationManager implements ReactiveAuthorizationManager<AuthorizationContext> {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Override
    public Mono<AuthorizationDecision> check(Mono<Authentication> authentication, AuthorizationContext context) {
        URI uri = context.getExchange().getRequest().getURI();
        List<String> authorities =
                ((List<String>) redisTemplate.opsForHash().get(RedisKey.RESOURCE_ROLES_MAP.getKey(), getPathPrefix(uri)));
        log.debug("path: {}, authorities: {}", uri.getPath(), authorities);
        if (authorities == null) {
            return Mono.just(new AuthorizationDecision(false));
        }
        return authentication
                .filter(Authentication::isAuthenticated)
                .doOnNext(auth -> log.debug("filter: {}", auth))
                .flatMapIterable(Authentication::getAuthorities)
                .map(GrantedAuthority::getAuthority)
                .any(authorities::contains)
                .map(AuthorizationDecision::new)
                .defaultIfEmpty(new AuthorizationDecision(false));
    }

    private String getPathPrefix(URI uri) {
        String path = uri.getPath();
        if (path.startsWith("/") && path.contains("/api")) {
            String[] split = path.split("/");
            if (split.length >= 3) {
                return Arrays.stream(split)
                        .limit(3)
                        .collect(Collectors.joining("/"));
            }
            return path.substring(0, path.lastIndexOf('/'));
        }
        return path;
    }
}