package com.bubble.pilipili.gateway.config;

import com.bubble.pilipili.common.constant.AuthConstant;
import com.bubble.pilipili.gateway.config.handler.MyAccessDeniedHandler;
import com.bubble.pilipili.gateway.config.handler.MyAuthenticationEntryPoint;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.core.convert.converter.Converter;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.oauth2.server.resource.authentication.ReactiveJwtAuthenticationConverterAdapter;
import org.springframework.security.web.server.SecurityWebFilterChain;
import reactor.core.publisher.Mono;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * @author liweixin@hcrc1.wecom.work
 * @date 2024/10/24
 */
@Slf4j
@AllArgsConstructor
@Configuration
@EnableWebFluxSecurity
public class ResourceServerConfig {

    @Autowired
    private AuthorizationManager authorizationManager;

    @Autowired
    private MyAccessDeniedHandler myAccessDeniedHandler;

    @Autowired
    private MyAuthenticationEntryPoint myAuthenticationEntryPoint;

    // 白名单URL
    private final List<String> ignoreUrlList;


    @PostConstruct
    public void init(){
        ignoreUrlList.add("/static");
        ignoreUrlList.add("/auth/oauth/**");
        ignoreUrlList.add("/auth/rsa/publicKey");
        ignoreUrlList.add("/doc.html");
        ignoreUrlList.add("/webjars/**");
        ignoreUrlList.add("/favicon.ico");
        ignoreUrlList.add("/v3/api-docs/**");
        ignoreUrlList.add("/doc/*/v3/api-docs/**");
        log.info("ignoreUrlList: {}", ignoreUrlList);
    }

    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
        http.oauth2ResourceServer().jwt()
                .jwtAuthenticationConverter(jwtConverter());
        http.authorizeExchange()
                // 白名单
                .pathMatchers(ignoreUrlList.toArray(new String[0])).permitAll()
                // 鉴权管理器
                .anyExchange().access(authorizationManager)
                .and().exceptionHandling()
                // 处理认证失败请求 401
                .authenticationEntryPoint(myAuthenticationEntryPoint)
                // 处理权限不足请求 403
                .accessDeniedHandler(myAccessDeniedHandler)
                .and().csrf().disable();

        return http.build();
    }

    @Bean
    public Converter<Jwt, ? extends Mono<? extends AbstractAuthenticationToken>> jwtConverter() {
        JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
        jwtGrantedAuthoritiesConverter.setAuthorityPrefix(AuthConstant.AUTHORITY_PREFIX);
        jwtGrantedAuthoritiesConverter.setAuthoritiesClaimName(AuthConstant.AUTHORITY_CLAIM_NAME);
        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(jwtGrantedAuthoritiesConverter);

        return new ReactiveJwtAuthenticationConverterAdapter(jwtAuthenticationConverter);
    }
}
