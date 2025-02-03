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
public class LoginReq {

    private String username;
    private String password;
    private String signature;
}
