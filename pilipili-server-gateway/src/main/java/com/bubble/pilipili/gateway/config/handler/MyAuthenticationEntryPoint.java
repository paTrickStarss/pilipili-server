/*
 * Copyright (c) 2024-2025. Bubble
 */

package com.bubble.pilipili.gateway.config.handler;

import com.bubble.pilipili.common.constant.AuthConstant;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.v3.core.util.Json;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.server.ServerAuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author liweixin@hcrc1.wecom.work
 * @date 2024/10/24
 */
@Slf4j
@Component
public class MyAuthenticationEntryPoint implements ServerAuthenticationEntryPoint {

    @Override
    public Mono<Void> commence(ServerWebExchange exchange, AuthenticationException ex) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> body = new HashMap<>();
        body.put("status", HttpStatus.UNAUTHORIZED.value());
        body.put("message", AuthConstant.UNAUTHORIZED_MSG);
        body.put("times", new Date());
        body.put("path", exchange.getRequest().getPath().value());

        try {
            DataBuffer wrap = response.bufferFactory().wrap(Json.pretty().writeValueAsBytes(body));
            return response.writeWith(Mono.just(wrap));
        } catch (JsonProcessingException e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }
}
