/*
 * Copyright (c) 2024. Bubble
 */

package com.bubble.pilipili.auth.controller;

import com.bubble.pilipili.auth.pojo.dto.OauthTokenDTO;
import com.bubble.pilipili.auth.pojo.req.OauthTokenReq;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

/**
 * @author liweixin@hcrc1.wecom.work
 * @date 2024/12/25
 */
@FeignClient("auth")
public interface OauthFeignAPI {

    @PostMapping("/oauth/token")
    OauthTokenDTO fetchToken(@Valid @RequestBody OauthTokenReq req);
}
