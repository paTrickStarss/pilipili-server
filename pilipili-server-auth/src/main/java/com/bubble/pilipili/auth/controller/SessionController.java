/*
 * Copyright (c) 2024. Bubble
 */

package com.bubble.pilipili.auth.controller;

import com.bubble.pilipili.auth.pojo.dto.LoginDTO;
import com.bubble.pilipili.auth.pojo.dto.OauthTokenDTO;
import com.bubble.pilipili.auth.pojo.req.LoginReq;
import com.bubble.pilipili.auth.pojo.req.OauthTokenReq;
import com.bubble.pilipili.auth.util.OauthUtil;
import com.bubble.pilipili.common.http.SimpleResponse;
import com.bubble.pilipili.common.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * @author liweixin@hcrc1.wecom.work
 * @date 2024/12/25
 */
@RestController
@RequestMapping("/session")
public class SessionController {

    @Autowired
    private OauthFeignAPI oauthFeignAPI;

    @PostMapping("/login")
    public SimpleResponse<LoginDTO> login(@Valid @RequestBody LoginReq req) {
        String username = req.getUsername();
        String password = req.getPassword();
        if (StringUtil.isEmpty(username) || StringUtil.isEmpty(password)) {
            return SimpleResponse.failed("请传入用户名和密码！");
        }

        OauthTokenReq oauthTokenReq = OauthUtil.getInstance().createOauthTokenReq(username, password);
        OauthTokenDTO oauthTokenDTO = oauthFeignAPI.fetchToken(oauthTokenReq);
        if (StringUtil.isEmpty(oauthTokenDTO.getError())) {
            return SimpleResponse.success(new LoginDTO(
                    Boolean.TRUE, oauthTokenDTO.getUsername(), oauthTokenDTO.getAccess_token()));
        } else {
            return SimpleResponse.failed(oauthTokenDTO.getError_description());
        }
    }
}
