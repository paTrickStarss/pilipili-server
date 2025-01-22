/*
 * Copyright (c) 2025. Bubble
 */

package com.bubble.pilipili.common.exception;

/**
 * @author Bubble
 * @date 2025/01/21 15:36
 */
public class ServiceOperationException extends RuntimeException {

    public ServiceOperationException(String message) {
        super(message);
    }

    public static ServiceOperationException emptyField(String fieldName) {
        return new ServiceOperationException("Service entity field [" + fieldName +"] is empty!");
    }
}
