/*
 * Copyright (c) 2024-2025. Bubble
 */

package com.bubble.pilipili.auth.pojo.dto;

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
public class OAuthTokenDTO {

    private String access_token;
    private String refresh_token;
    private String token_type;
    private Long expires_in;
    private String scope;
    private String jti;
    private String username;

    private String error;
    private String error_description;
}
