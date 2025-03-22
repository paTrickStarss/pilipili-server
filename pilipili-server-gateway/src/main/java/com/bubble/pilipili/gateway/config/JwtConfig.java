///*
// * Copyright (c) 2025. Bubble
// */
//
//package com.bubble.pilipili.gateway.config;
//
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.oauth2.jwt.JwtDecoder;
//import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
//
///**
// * @author Bubble
// * @date 2025.03.22 16:58
// */
//@Configuration
//public class JwtConfig {
//
//    @Value("${spring.security.oauth2.resourceserver.jwt.jwk-set-uri}")
//    private String jwkSetUri;
//
//    @Bean
//    public JwtDecoder myJwtDecoder() {
//        return NimbusJwtDecoder.withJwkSetUri(jwkSetUri).build();
//    }
//
//}
