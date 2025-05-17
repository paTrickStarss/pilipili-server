/*
 * Copyright (c) 2024-2025. Bubble
 */

package com.bubble.pilipili.auth.pojo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 登录返回数据
 * @author liweixin@hcrc1.wecom.work
 * @date 2024/12/25
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginDTO {

    private String username;
    private String accessToken;
    private Long expires;
    private Boolean admin;
}
