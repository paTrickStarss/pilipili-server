/*
 * Copyright (c) 2025. Bubble
 */

package com.bubble.pilipili.common.http;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * 通用分页查询请求参数
 * @author Bubble
 * @date 2025.03.01 16:32
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PageQueryReq implements Serializable {

    @NotBlank(message = "请传入分页序号")
    private Long pageNo;

    @NotBlank(message = "请传入分页大小")
    @Max(value = 100, message = "分页大小不能大于100")
    private Long pageSize;

    public Long getPageNo() {
        return pageNo == null ? 1L : pageNo;
    }

    public Long getPageSize() {
        return pageSize == null ? 10L : pageSize;
    }
}
