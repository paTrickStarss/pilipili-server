/*
 * Copyright (c) 2025. Bubble
 */

package com.bubble.pilipili.common.config;

import com.bubble.pilipili.common.exception.BadRequestException;
import com.bubble.pilipili.common.exception.ForbiddenException;
import com.bubble.pilipili.common.http.SimpleResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Bubble
 * @date 2025.03.04 11:17
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 请求参数异常
     * @param ex
     * @return
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationException(MethodArgumentNotValidException ex) {
        Map<String, String> errorMap = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error ->
            errorMap.put(
                    ((FieldError) error).getField(),
                    error.getDefaultMessage())
        );
        return ResponseEntity.badRequest().body(errorMap);
    }

    /**
     * 权限不足异常
     * @param ex
     * @return
     */
    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<SimpleResponse<String>> handleForbiddenException(ForbiddenException ex) {
        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(SimpleResponse.failed(ex.getMessage()));
    }

    /**
     * 请求内容异常
     * @param ex
     * @return
     */
    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<SimpleResponse<String>> handleBadRequestException(BadRequestException ex) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(SimpleResponse.failed(ex.getMessage()));
    }

    /**
     * 服务端异常
     * @param ex
     * @return
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<SimpleResponse<String>> handleException(Exception ex) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(SimpleResponse.error(ex.getMessage()));
    }
}
