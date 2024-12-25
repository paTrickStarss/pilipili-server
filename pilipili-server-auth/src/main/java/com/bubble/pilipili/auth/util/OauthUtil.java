/*
 * Copyright (c) 2024. Bubble
 */

package com.bubble.pilipili.auth.util;

import com.bubble.pilipili.auth.pojo.req.OauthTokenReq;

/**
 * @author liweixin@hcrc1.wecom.work
 * @date 2024/12/25
 */
public class OauthUtil {

    private OauthUtil() {}
    private static class lazyHolder {
        private static final OauthUtil INSTANCE = new OauthUtil();
    }
    public static OauthUtil getInstance() {
        return lazyHolder.INSTANCE;
    }

    public static final String OAUTH_GRANT_TYPE = "password";
    public static final String OAUTH_CLIENT_ID = "client";
    public static final String OAUTH_CLIENT_SECRET = "bubble233";

    public OauthTokenReq createOauthTokenReq(String username, String password) {
        OauthTokenReq oauthTokenReq = new OauthTokenReq();
        oauthTokenReq.setGrant_type(OAUTH_GRANT_TYPE);
        oauthTokenReq.setClient_id(OAUTH_CLIENT_ID);
        oauthTokenReq.setClient_secret(OAUTH_CLIENT_SECRET);
        oauthTokenReq.setUsername(username);
        oauthTokenReq.setPassword(password);
        return oauthTokenReq;
    }
}
