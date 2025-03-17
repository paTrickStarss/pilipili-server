/*
 * Copyright (c) 2025. Bubble
 */

package com.bubble.pilipili.video.pojo.req;

import com.bubble.pilipili.video.pojo.entity.CollectionVideo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.List;

/**
 * @author Bubble
 * @date 2025.03.17 17:09
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChangeCollectionVideoReq implements Serializable {

    /**
     * 收藏夹视频关系列表
     */
    @NotEmpty(message = "收藏夹视频关系列表不能为空")
    private List<CollectionVideo> collectionVideoList;
}
