/*
 * Copyright (c) 2025. Bubble
 */

package com.bubble.pilipili.common.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 用户角色枚举
 * @author Bubble
 * @date 2025.05.15 17:24
 */
@Getter
@AllArgsConstructor
public enum UserRole {

    ROLE_USER(0, "USER", "普通用户"),
    ROLE_ADMIN(1,"ADMIN", "管理员用户"),
//    ROLE_TEST(2, "TEST", "测试用户"),
    ;

    private final Integer code;
    private final String value;
    private final String name;
}
