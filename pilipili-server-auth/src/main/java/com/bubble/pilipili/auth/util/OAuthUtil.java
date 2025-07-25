/*
 * Copyright (c) 2024-2025. Bubble
 */

package com.bubble.pilipili.auth.util;

import com.bubble.pilipili.auth.pojo.req.OAuthTokenReq;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

/**
 * @author liweixin@hcrc1.wecom.work
 * @date 2024/12/25
 */
public class OAuthUtil {

    private OAuthUtil() {}
    private static class lazyHolder {
        private static final OAuthUtil INSTANCE = new OAuthUtil();
    }
    public static OAuthUtil getInstance() {
        return lazyHolder.INSTANCE;
    }

    /**
     * JWT密钥对文件名
     */
    public static final String FILE_NAME_JWT_JKS = "jwt.jks";
    /**
     * JWT密钥对名称
     */
    public static final String JWT_KEY_NAME = "pilipili_jwt_key";
    /**
     * scopes
     */
    public static final String[] OAUTH_SCOPES = new String[]{"all"};
    /**
     * AuthorizeGrantTypes
     */
    public static final String[] OAUTH_AUTHORIZED_GRANT_TYPES =
            new String[]{"authorization_code", "password", "refresh_token"};
    /**
     * OAuth鉴权类型
     */
    public static final String OAUTH_GRANT_TYPE = "password";
    /**
     * client_id
     */
    public static final String OAUTH_CLIENT_ID = "client";
    /**
     * client_secret
     */
    public static final String OAUTH_CLIENT_SECRET = "bubble233";
    /**
     * accessToken 有效期 3天
     */
    public static final int OAUTH_ACCESS_TOKEN_EXPIRES =  3 * 24 * 60 * 60; //86400;
    /**
     * refreshToken 有效期 30天
     */
    public static final int OAUTH_REFRESH_TOKEN_EXPIRES = 30 * 24 * 60 * 60; //604800;

    public OAuthTokenReq createOauthTokenReq(String username, String password) {
        OAuthTokenReq oauthTokenReq = new OAuthTokenReq();
        oauthTokenReq.setGrant_type(OAUTH_GRANT_TYPE);
        oauthTokenReq.setClient_id(OAUTH_CLIENT_ID);
        oauthTokenReq.setClient_secret(OAUTH_CLIENT_SECRET);
        oauthTokenReq.setUsername(username);
        oauthTokenReq.setPassword(password);
        return oauthTokenReq;
    }

    public MultiValueMap<String, String> getOauthTokenReqParams(OAuthTokenReq oauthTokenReq) {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", oauthTokenReq.getGrant_type());
        params.add("client_id", oauthTokenReq.getClient_id());
        params.add("client_secret", oauthTokenReq.getClient_secret());
        params.add("username", oauthTokenReq.getUsername());
        params.add("password", oauthTokenReq.getPassword());
        return params;
    }

    public MultiValueMap<String, String> getOauthTokenReqParams(String username, String password) {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", OAUTH_GRANT_TYPE);
        params.add("client_id", OAUTH_CLIENT_ID);
        params.add("client_secret", OAUTH_CLIENT_SECRET);
        params.add("username", username);
        params.add("password", password);
        return params;
    }

    public MultiValueMap<String, String> getOauthTokenReqHeaders() {
        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        headers.add("Content-Type", "application/x-www-form-urlencoded");
        headers.add("Host", "localhost:8000");
        headers.add("Accept", "*/*");
        headers.add("Connection", "keep-alive");
        return headers;
    }
}
