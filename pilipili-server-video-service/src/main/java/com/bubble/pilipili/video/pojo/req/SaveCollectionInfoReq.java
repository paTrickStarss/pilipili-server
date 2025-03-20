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
 * @date 2025.03.17 16:10
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SaveCollectionInfoReq implements Serializable {

    /**
     * 创建者ID
     */
    @NotBlank(message = "创建用户UID不能为空")
    private Integer uid;
    /**
     * 标题
     */
    @NotBlank(message = "标题不能为空")
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
