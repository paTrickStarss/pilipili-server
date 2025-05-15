/*
 * Copyright (c) 2025. Bubble
 */

package com.bubble.pilipili.common.exception;

/**
 * 接口权限不足异常
 * @author Bubble
 * @date 2025.05.15 17:16
 */
public class ForbiddenException extends RuntimeException {
    public ForbiddenException(String message) {
        super(message);
    }
}
