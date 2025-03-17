/*
 * Copyright (c) 2025. Bubble
 */

package com.bubble.pilipili.video.pojo.req;

import com.bubble.pilipili.common.http.PageQueryReq;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

/**
 * @author Bubble
 * @date 2025.03.17 16:26
 */
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PageQueryCollectionInfoReq extends PageQueryReq {

    @NotBlank(message = "创建者UID不能为空")
    private Integer uid;
}
