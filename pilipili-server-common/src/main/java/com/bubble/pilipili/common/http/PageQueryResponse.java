/*
 * Copyright (c) 2025. Bubble
 */

package com.bubble.pilipili.common.http;

import com.bubble.pilipili.common.pojo.PageDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.io.Serializable;
import java.util.List;

/**
 * 分页查询响应体数据类型
 * @author Bubble
 * @date 2025/01/22 23:53
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PageQueryResponse<T> implements Response, Serializable {

    private int code;
    private String message;
    private Long pageNo;
    private Long pageSize;
    private Long total;
    private List<T> data;

    public static <T> PageQueryResponse<T> success(List<T> data, Long pageNo, Long pageSize, Long total) {
        return new PageQueryResponse<>(HttpStatus.OK.value(), "", pageNo, pageSize, total, data);
    }
    public static <T> PageQueryResponse<T> success(PageDTO<T> pageDTO) {
        return new PageQueryResponse<>(HttpStatus.OK.value(), "",
                pageDTO.getPageNo(), pageDTO.getPageSize(), pageDTO.getTotal(), pageDTO.getData());
    }

    public static <T> PageQueryResponse<T> failed(String message) {
        return new PageQueryResponse<>(HttpStatus.BAD_REQUEST.value(), message,
                null, null, null, null);
    }
}
