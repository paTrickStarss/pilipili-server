/*
 * Copyright (c) 2025. Bubble
 */

package com.bubble.pilipili.video.pojo.req;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * @author Bubble
 * @date 2025.03.17 16:16
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateCollectionInfoReq implements Serializable {

    /**
     * 收藏夹ID
     */
    @NotBlank(message = "收藏夹ID不能为空")
    private Integer collectionId;
    /**
     * 标题
     */
    private String title;
    /**
     * 介绍
     */
    private String description;
    /**
     * 封面链接
     */
    private String coverUrl;
    /**
     * 私密
     */
    @Min(value = 0, message = "标志位值为0或1")
    @Max(value = 1, message = "标志位值为0或1")
    private Integer himitsu;
}
