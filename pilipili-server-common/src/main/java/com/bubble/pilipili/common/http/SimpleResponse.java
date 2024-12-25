/*
 * Copyright (c) 2024. Bubble
 */

package com.bubble.pilipili.common.http;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.io.Serializable;

/**
 * @author liweixin@hcrc1.wecom.work
 * @date 2024/10/23
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SimpleResponse<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    private int code;
    private String message;
    private T data;

    public static <T> SimpleResponse<T> success(T data) {
        return new SimpleResponse<>(HttpStatus.OK.value(), "", data);
    }

    public static <T> SimpleResponse<T> failed(String message) {
        return new SimpleResponse<>(HttpStatus.BAD_REQUEST.value(), message, null);
    }
}
