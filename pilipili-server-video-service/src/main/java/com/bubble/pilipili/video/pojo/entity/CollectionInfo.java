/*
 * Copyright (c) 2025. Bubble
 */

package com.bubble.pilipili.video.pojo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @author Bubble
 * @date 2025.03.17 15:33
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("collection_info")
public class CollectionInfo {

    /**
     * 收藏夹ID
     */
    @TableId(type = IdType.AUTO)
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
     * 删除
     */
    private Integer rm;
}