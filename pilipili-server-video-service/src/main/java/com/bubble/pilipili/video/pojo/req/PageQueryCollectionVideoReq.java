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
 * @date 2025.03.17 17:14
 */
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PageQueryCollectionVideoReq extends PageQueryReq {

    /**
     * 收藏夹ID
     */
    @NotBlank(message = "收藏夹ID不能为空")
    private Integer collectionId;

}
