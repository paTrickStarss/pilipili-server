/*
 * Copyright (c) 2025. Bubble
 */

package com.bubble.pilipili.video.pojo.req;

import com.bubble.pilipili.common.http.PageQueryReq;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

/**
 * @author Bubble
 * @date 2025/01/20 18:25
 */
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PageQueryVideoInfoReq extends PageQueryReq {

    /**
     * 查询用户uid，-1表示全部用户
     */
    @NotEmpty
    private Integer uid;

    private String keyword;
    private String title;
    private String tag;
    private String publishDateStart;
    private String publishDateEnd;

    private Integer status;
}
