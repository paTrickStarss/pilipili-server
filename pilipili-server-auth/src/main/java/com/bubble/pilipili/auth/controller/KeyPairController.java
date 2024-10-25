package com.bubble.pilipili.auth.controller;

import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.KeyPair;
import java.security.interfaces.RSAPublicKey;
import java.util.Map;

/**
 * 密钥对接口
 * @author liweixin@hcrc1.wecom.work
 * @date 2024/10/24
 */
@RestController
public class KeyPairController {

    @Autowired
    private KeyPair keyPair;

    /**
     * 公钥获取接口
     * @return
     */
    @GetMapping("/rsa/publicKey")
    public Map<String, Object> publicKey() {
        RSAPublicKey publicKey = ((RSAPublicKey) keyPair.getPublic());
        RSAKey rsaKey = new RSAKey.Builder(publicKey).build();
        return new JWKSet(rsaKey).toJSONObject();
    }
}
