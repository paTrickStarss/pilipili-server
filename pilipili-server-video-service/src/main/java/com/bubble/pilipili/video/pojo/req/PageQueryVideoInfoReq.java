/*
 * Copyright (c) 2025. Bubble
 */

package com.bubble.pilipili.video.pojo.req;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Bubble
 * @date 2025/01/20 18:25
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PageQueryVideoInfoReq {

    private Long pageNo;
    private Long pageSize;

    private Integer uid;

    private String keyword;
    private String title;
    private String tag;
    private String publishDateStart;
    private String publishDateEnd;
}
