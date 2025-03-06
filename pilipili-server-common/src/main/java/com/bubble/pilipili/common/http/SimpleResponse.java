/*
 * Copyright (c) 2024-2025. Bubble
 */

package com.bubble.pilipili.common.http;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.io.Serializable;

/**
 * 响应体通用数据类型
 * @author liweixin@hcrc1.wecom.work
 * @date 2024/10/23
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SimpleResponse<T> implements Response, Serializable {

    private int code;
    private String msg;
    private T data;

    public static <T> SimpleResponse<T> success(T data) {
        return new SimpleResponse<>(HttpStatus.OK.value(), "", data);
    }
    public static <T> SimpleResponse<T> success(String msg) {
        return new SimpleResponse<>(HttpStatus.OK.value(), msg, null);
    }
    public static <T> SimpleResponse<T> result(Boolean res) {
        return res? success("操作成功"):failed("操作失败");
    }

    public static <T> SimpleResponse<T> failed(String message) {
        return new SimpleResponse<>(HttpStatus.BAD_REQUEST.value(), message, null);
    }
    public static <T> SimpleResponse<T> error(String message) {
        return new SimpleResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), message, null);
    }
}
