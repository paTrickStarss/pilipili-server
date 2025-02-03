/*
 * Copyright (c) 2025. Bubble
 */

package com.bubble.pilipili.user.pojo.req;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Bubble
 * @date 2025/02/03 23:39
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterReq {
    /**
     * 昵称
     */
    private String nickname;
    /**
     * 密码（加密）
     */
    private String password;
    /**
     * 签名
     */
    private String signature;
    /**
     * 电子邮箱
     */
    private String email;
}
