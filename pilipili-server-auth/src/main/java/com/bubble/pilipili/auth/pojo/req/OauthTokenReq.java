/*
 * Copyright (c) 2024. Bubble
 */

package com.bubble.pilipili.auth.pojo.req;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author liweixin@hcrc1.wecom.work
 * @date 2024/12/25
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OauthTokenReq {

    private String grant_type;
    private String client_id;
    private String client_secret;
    private String username;
    private String password;

}
