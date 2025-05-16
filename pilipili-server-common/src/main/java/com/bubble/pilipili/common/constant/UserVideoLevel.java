/*
 * Copyright (c) 2025. Bubble
 */

package com.bubble.pilipili.common.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Bubble
 * @date 2025.05.15 18:05
 */
@Getter
@AllArgsConstructor
public enum UserVideoLevel {

    /**
     * 上传中
     */
    LEVEL_UPLOADING("UPLOADING", 0),
    /**
     * 审核中
     */
    LEVEL_AUDITING("AUDITING", 1),
    /**
     * 公开展示（审核通过）
     */
    LEVEL_PUBLIC("PUBLIC", 2),
    /**
     * 审核不通过
     */
    LEVEL_AUDITING_FAILED("AUDITING_FAILED", 3),
    /**
     * 下架
     */
    LEVEL_BLOCK("BLOCK", 4),
    /**
     * 用户个人（上传中、审核中、审核通过）
     */
    LEVEL_USER("USER", 5),
    /**
     * 管理员（全部状态）
     */
    LEVEL_ADMIN("ADMIN", 6),
    ;

    private final String key;
    private final Integer code;
}
