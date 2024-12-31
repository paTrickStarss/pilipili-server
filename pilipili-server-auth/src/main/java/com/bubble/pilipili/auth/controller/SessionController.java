/*
 * Copyright (c) 2024. Bubble
 */

package com.bubble.pilipili.auth.controller;

import com.bubble.pilipili.common.component.SessionManager;
import com.bubble.pilipili.auth.pojo.dto.LoginDTO;
import com.bubble.pilipili.auth.pojo.dto.OAuthTokenDTO;
import com.bubble.pilipili.auth.pojo.req.LoginReq;
import com.bubble.pilipili.auth.util.OAuthUtil;
import com.bubble.pilipili.common.http.SimpleResponse;
import com.bubble.pilipili.common.util.StringUtil;
import com.nimbusds.jose.JWSObject;
import feign.FeignException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.text.ParseException;
import java.util.Map;

/**
 * 用户会话控制器
 * @author liweixin@hcrc1.wecom.work
 * @date 2024/12/25
 */
@Slf4j
@RestController
@RequestMapping("/session")
public class SessionController {

    @Autowired
    private OAuthFeignAPI oAuthFeignAPI;

    @Autowired
    private SessionManager sessionManager;

    @PostMapping("/login")
    public SimpleResponse<LoginDTO> login(@Valid @RequestBody LoginReq req) {
        String username = req.getUsername();
        String password = req.getPassword();
        if (StringUtil.isEmpty(username) || StringUtil.isEmpty(password)) {
            return SimpleResponse.failed("请传入用户名和密码！");
        }

        MultiValueMap<String, String> params = OAuthUtil.getInstance().getOauthTokenReqParams(username, password);
        MultiValueMap<String, String> header = OAuthUtil.getInstance().getOauthTokenReqHeaders();
        try {
            OAuthTokenDTO oAuthTokenDTO = oAuthFeignAPI.fetchToken(params, header);

            // 更新用户token有效状态
            JWSObject jwsObject = JWSObject.parse(oAuthTokenDTO.getAccess_token());
            Map<String, Object> jsonObject = jwsObject.getPayload().toJSONObject();
            String jti = jsonObject.get("jti").toString();
            sessionManager.saveToken(oAuthTokenDTO.getUsername(), jti, oAuthTokenDTO.getExpires_in());

            return SimpleResponse.success(new LoginDTO(oAuthTokenDTO.getUsername(), oAuthTokenDTO.getAccess_token()));
        } catch (FeignException e) {
            log.warn("Token获取异常: {}", e.getMessage());
            if (e.getMessage().contains("invalid_grant")) {
                return SimpleResponse.failed("用户名或密码错误");
            }
            return SimpleResponse.error("服务端异常");
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/logout")
    public SimpleResponse<Void> logout(@Valid @RequestParam String username) {
        if (StringUtil.isEmpty(username)) {
            return SimpleResponse.failed("请传入用户名！");
        }

        Boolean result = sessionManager.removeToken(username);
        if (result) {
            return SimpleResponse.success("登出成功！");
        } else {
            return SimpleResponse.failed("用户不存在或已登出！");
        }
    }
}
