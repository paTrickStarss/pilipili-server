/*
 * Copyright (c) 2025. Bubble
 */

package com.bubble.pilipili.stats.repository.impl;

import com.bubble.pilipili.common.repository.impl.CommonRepoImpl;
import com.bubble.pilipili.stats.mapper.VideoStatsMapper;
import com.bubble.pilipili.feign.pojo.entity.VideoStats;
import com.bubble.pilipili.stats.repository.VideoStatsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * @author Bubble
 * @date 2025.03.09 14:43
 */
@Component
public class VideoStatsRepositoryImpl implements VideoStatsRepository {

    @Autowired
    private VideoStatsMapper videoStatsMapper;

    /**
     * 保存统计数据
     *
     * @param entity
     * @return
     */
    @Override
    public Boolean saveStats(VideoStats entity) {
        return CommonRepoImpl.saveStats(
                entity,
                videoStatsMapper,
                VideoStats::getVid,
                VideoStats::getViewCount,
                VideoStats::getDanmakuCount,
                VideoStats::getFavorCount,
                VideoStats::getCoinCount,
                VideoStats::getCollectCount,
                VideoStats::getRepostCount,
                VideoStats::getDewCount
        );
    }

    /**
     * 批量查询统计数据
     *
     * @param idList
     * @return Map(id, entity)
     */
    @Override
    public Map<Integer, VideoStats> getStats(List<Integer> idList) {
        return CommonRepoImpl.getStats(idList, VideoStats::getVid, videoStatsMapper);
    }
}
