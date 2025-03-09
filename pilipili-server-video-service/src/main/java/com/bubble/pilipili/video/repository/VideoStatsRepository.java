/*
 * Copyright (c) 2025. Bubble
 */

package com.bubble.pilipili.video.repository;

import com.bubble.pilipili.video.pojo.entity.VideoStats;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @author Bubble
 * @date 2025.03.09 14:42
 */
@Repository
public interface VideoStatsRepository {

    /**
     * 保存视频统计数据
     * @param videoStats
     * @return
     */
    Boolean saveVideoStats(VideoStats videoStats);

    /**
     * 批量获取视频统计数据（直接从表中查询）
     * @param vidList
     * @return
     */
    Map<Integer, VideoStats> getVideoStats(List<Integer> vidList);
}
