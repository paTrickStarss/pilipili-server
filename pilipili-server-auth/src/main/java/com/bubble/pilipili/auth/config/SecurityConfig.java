/*
 * Copyright (c) 2024-2025. Bubble
 */

package com.bubble.pilipili.auth.config;

import com.bubble.pilipili.common.config.SecurityConfigProperties;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.autoconfigure.security.servlet.EndpointRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * @author liweixin@hcrc1.wecom.work
 * @date 2024/10/23
 */
@Slf4j
@AllArgsConstructor
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private SecurityConfigProperties properties;

    // 白名单URL
    private final List<String> ignoreUrlList;

    @PostConstruct
    public void init(){
        ignoreUrlList.addAll(properties.getIgnoreUrlList());
        log.info("ignoreUrlList: {}", ignoreUrlList);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .requestMatchers(EndpointRequest.toAnyEndpoint()).permitAll()
                // 开放访问白名单  默认只允许GET请求  POST请求若没有CSRF令牌则会被CSRF拦截
                .antMatchers(ignoreUrlList.toArray(new String[0])).permitAll()
                .anyRequest().authenticated()
                // CSRF白名单
                .and().csrf().ignoringAntMatchers(ignoreUrlList.toArray(new String[0]));
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
}
