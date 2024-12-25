/*
 * Copyright (c) 2024. Bubble
 */

package com.bubble.pilipili.gateway.filter;

import com.bubble.pilipili.common.constant.AuthConstant;
import com.nimbusds.jose.JWSObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.text.ParseException;

/**
 * 认证全局过滤器
 * @author liweixin@hcrc1.wecom.work
 * @date 2024/10/24
 */
@Slf4j
@Configuration
public class AuthGlobalFilter implements GlobalFilter, Ordered {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        String authorization = request.getHeaders().getFirst("Authorization");
        if (authorization == null || authorization.isEmpty()) {
            return chain.filter(exchange);  // Reject
        }
        try {
            // 将token转换为用户信息再放回请求头
            String token = authorization.replace("Bearer ", "");
            JWSObject jwsObject = JWSObject.parse(token);
            String userStr = jwsObject.getPayload().toString();
            log.debug("AuthGlobalFilter.filter() user: {}", userStr);

            request.mutate().header(AuthConstant.JWT_PAYLOAD_HEADER, userStr).build();
            exchange = exchange.mutate().request(request).build();

        } catch (ParseException e) {
            log.error(e.getMessage());
            e.printStackTrace();
        }


        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
