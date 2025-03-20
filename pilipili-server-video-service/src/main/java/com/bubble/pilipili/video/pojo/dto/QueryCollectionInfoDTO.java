/*
 * Copyright (c) 2025. Bubble
 */

package com.bubble.pilipili.video.pojo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author Bubble
 * @date 2025.03.17 16:20
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class QueryCollectionInfoDTO implements Serializable {

    /**
     * 收藏夹ID
     */
    private Integer collectionId;
    /**
     * 创建者ID
     */
    private Integer uid;
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
    private Integer himitsu;
    /**
     * 创建时间
     */
    private LocalDateTime createTime;


    /**
     * 收藏夹视频数量
     */
    private Long videoCount = 0L;
}
