/*
 * Copyright (c) 2025. Bubble
 */

package com.bubble.pilipili.video.pojo.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Bubble
 * @date 2025.03.17 15:40
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("collection_video")
public class CollectionVideo {

    /**
     * 收藏夹ID
     */
    private Integer collectionId;
    /**
     * 视频ID
     */
    private Integer vid;
}
