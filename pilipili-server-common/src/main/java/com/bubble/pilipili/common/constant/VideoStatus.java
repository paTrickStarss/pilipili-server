/*
 * Copyright (c) 2025. Bubble
 */

package com.bubble.pilipili.common.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Bubble
 * @date 2025.05.13 21:15
 */
@Getter
@AllArgsConstructor
public enum VideoStatus {

    UPLOADING(0, "上传中"),
    AUDITING(1, "审核中"),
    AUDIT_PASSED(2, "审核通过"),
    AUDIT_FAILED(3, "审核不通过"),
    BLOCK(4, "下架"),
    ;

    private final Integer value;
    private final String description;
}
