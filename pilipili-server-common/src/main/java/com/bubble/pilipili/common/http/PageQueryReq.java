/*
 * Copyright (c) 2025. Bubble
 */

package com.bubble.pilipili.common.http;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 通用分页查询请求参数
 * @author Bubble
 * @date 2025.03.01 16:32
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PageQueryReq {

    private Long pageNo;
    private Long pageSize;

}
