///*
// * Copyright (c) 2025. Bubble
// */
//
//package com.bubble.pilipili.gateway.config;
//
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.http.HttpCookie;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.oauth2.jwt.Jwt;
//import org.springframework.security.oauth2.jwt.JwtDecoder;
//import org.springframework.security.oauth2.jwt.JwtException;
//import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
//import org.springframework.security.oauth2.server.resource.BearerTokenAuthenticationToken;
//import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
//import org.springframework.security.web.server.authentication.ServerAuthenticationConverter;
//import org.springframework.stereotype.Component;
//import org.springframework.web.server.ServerWebExchange;
//import reactor.core.publisher.Mono;
//
///**
// * 自定义BearerToken从Cookies提取
// * @author Bubble
// * @date 2025.03.22 15:31
// */
//@Slf4j
//@Component
//public class CookieServerAuthenticationConverter implements ServerAuthenticationConverter {
//
//    @Autowired
//    private JwtDecoder myJwtDecoder;
//
////    public CookieServerAuthenticationConverter(JwtDecoder jwtDecoder) {
////        jwtDecoder = jwtDecoderByPublicKeyValue()
////    }
//
//    /**
//     * Converts a {@link ServerWebExchange} to an {@link Authentication}
//     *
//     * @param exchange The {@link ServerWebExchange}
//     * @return A {@link Mono} representing an {@link Authentication}
//     */
//    @Override
//    public Mono<Authentication> convert(ServerWebExchange exchange) {
//
//        HttpCookie tokenCookie = exchange.getRequest().getCookies().getFirst("access_token");
//        if (tokenCookie == null) {
//            return Mono.empty();
//        }
//        String token = tokenCookie.getValue();
//        token = token.replace("%20", " ");
//
////        return Mono.just(new BearerTokenAuthenticationToken(token));
//
//        token = token.replace("Baerer ", "");
//        try {
//            Jwt jwt = myJwtDecoder.decode(token);
//            return Mono.just(new JwtAuthenticationToken(jwt));
//        } catch (JwtException e) {
//            log.error("Jwt解析失败: {}", e.getMessage());
//            return Mono.empty();
//        }
//    }
//}
