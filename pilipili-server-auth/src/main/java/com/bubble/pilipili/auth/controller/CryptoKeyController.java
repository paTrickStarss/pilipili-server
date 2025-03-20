/*
 * Copyright (c) 2025. Bubble
 */

package com.bubble.pilipili.auth.controller;

import com.bubble.pilipili.common.component.CryptoHelper;
import com.bubble.pilipili.common.http.Controller;
import com.bubble.pilipili.common.http.SimpleResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Bubble
 * @date 2025.03.20 17:37
 */
@RestController
@RequestMapping("/crypto")
public class CryptoKeyController implements Controller {


    @Autowired
    private CryptoHelper cryptoHelper;

    /**
     * 前端参数加密公钥
     * @return
     */
    @GetMapping("/publicKey")
    public SimpleResponse<Map<String, Object>> publicKey() {
        String publicKey = cryptoHelper.getPublicKeyStrWrapped();
        Map<String, Object> result = new HashMap<>();
        result.put("publicKey", publicKey);
        return SimpleResponse.success(result);
    }
}
