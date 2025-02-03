/*
 * Copyright (c) 2024. Bubble
 */

package com.bubble.pilipili.gateway.filter;

import com.alibaba.fastjson2.JSON;
import com.bubble.pilipili.common.constant.AuthConstant;
import com.bubble.pilipili.common.component.SessionManager;
import com.nimbusds.jose.JWSObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.http.HttpCookie;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.util.MultiValueMap;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

/**
 * 认证全局过滤器
 * @author liweixin@hcrc1.wecom.work
 * @date 2024/10/24
 */
@Slf4j
@Configuration
public class AuthGlobalFilter implements GlobalFilter, Ordered {

    @Autowired
    private SessionManager sessionManager;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();

        if (request.getPath().toString().contains("/login") || request.getPath().toString().contains("/register")) {
            return chain.filter(exchange);
        }
        // TODO: 请求没带Authorization请求头 会被上级的过滤掉 到不了这里 需要改变鉴权方式（目前是 BearerToken）为 Cookies
//        MultiValueMap<String, HttpCookie> cookies = request.getCookies();
//        log.debug("request cookies: {}", cookies);
//        HttpCookie accessToken = cookies.getFirst("accessToken");
//        if (accessToken == null) {
//            ServerHttpResponse response = exchange.getResponse();
//            response.setStatusCode(HttpStatus.UNAUTHORIZED);
//            return response.setComplete();
//        }
//        String token = accessToken.getValue();

        String authorization = request.getHeaders().getFirst("Authorization");
        if (authorization == null || authorization.isEmpty()) {
            ServerHttpResponse response = exchange.getResponse();
            response.setStatusCode(HttpStatus.UNAUTHORIZED);
            return response.setComplete();
        }
        String token = authorization.replace("Bearer ", "");
        try {
            // 将token转换为用户信息再放回请求头
            JWSObject jwsObject = JWSObject.parse(token);
            String userStr = jwsObject.getPayload().toString();
//            Map payloadMap = JSON.parseObject(userStr, Map.class);
            Map<String, Object> payloadMap = jwsObject.getPayload().toJSONObject();
            String jti = (String) payloadMap.get("jti");
            String username = (String) payloadMap.get("username");

            boolean valid = sessionManager.checkToken(username, jti);
            if (!valid) {
                log.debug("Token无效！ username: {}, jti: {}", username, jti);
                ServerHttpResponse response = exchange.getResponse();
                response.setStatusCode(HttpStatus.UNAUTHORIZED);
                DataBufferFactory dataBufferFactory = response.bufferFactory();
                Map<String, Object> body = new HashMap<>();
                body.put("msg", "Token无效！");
                DataBuffer dataBuffer = dataBufferFactory.wrap(JSON.toJSONBytes(body));
                return response.writeWith(Mono.just(dataBuffer)).then(Mono.empty());
//                return response.setComplete();
            }

            log.debug("AuthGlobalFilter.filter() user: {}", userStr);

            request.mutate().header(AuthConstant.JWT_PAYLOAD_HEADER, userStr).build();
            exchange = exchange.mutate().request(request).build();

        } catch (Exception e) {
            log.error("Token解析异常！{}", e.getMessage());
            ServerHttpResponse response = exchange.getResponse();
            response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
            return response.setComplete();
        }


        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
