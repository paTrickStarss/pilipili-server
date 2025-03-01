/*
 * Copyright (c) 2025. Bubble
 */

package com.bubble.pilipili.common.exception;

/**
 * 数据库操作异常
 * @author Bubble
 * @date 2025.03.01 17:21
 */
public class RepositoryException extends RuntimeException {
    public RepositoryException(String message) {
        super(message);
    }
}
