/*
 * Copyright (c) 2025. Bubble
 */

package com.bubble.pilipili.video.repository;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bubble.pilipili.video.pojo.entity.VideoInfo;
import com.bubble.pilipili.video.pojo.param.QueryVideoInfoParam;

/**
 * @author Bubble
 * @date 2025/01/20 18:41
 */
public interface VideoInfoRepository {

    Boolean saveVideoInfo(VideoInfo videoInfo);
    Boolean updateVideoInfo(VideoInfo videoInfo);
    Boolean deleteVideoInfo(Integer vid);
    VideoInfo getVideoInfoById(Integer vid);
    Page<VideoInfo> pageQueryVideoInfoByUid(Integer uid, Long pageNo, Long pageSize);
    Page<VideoInfo> pageQueryVideoInfo(QueryVideoInfoParam param, Long pageNo, Long pageSize);
}
