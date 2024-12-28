/*
 * Copyright (c) 2024. Bubble
 */

package com.bubble.pilipili.auth.controller;

import com.bubble.pilipili.auth.pojo.dto.OAuthTokenDTO;
import com.bubble.pilipili.auth.pojo.req.OAuthTokenReq;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;

/**
 * @author liweixin@hcrc1.wecom.work
 * @date 2024/12/25
 */
@FeignClient("auth")
public interface OAuthFeignAPI {

    @PostMapping("/oauth/token")
    OAuthTokenDTO fetchToken(@Valid @RequestBody OAuthTokenReq req);

    @PostMapping("/oauth/token")
    OAuthTokenDTO fetchToken(@RequestParam MultiValueMap<String, String> params, @RequestHeader MultiValueMap<String, String> headers);

}
