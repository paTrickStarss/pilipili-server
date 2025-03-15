/*
 * Copyright (c) 2025. Bubble
 */

package com.bubble.pilipili.stats.service.impl;

import com.bubble.pilipili.feign.pojo.dto.QueryStatsDTO;
import com.bubble.pilipili.feign.pojo.entity.VideoStats;
import com.bubble.pilipili.stats.repository.VideoStatsRepository;
import com.bubble.pilipili.stats.service.VideoStatsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author Bubble
 * @date 2025.03.15 20:29
 */
@Service
public class VideoStatsServiceImpl implements VideoStatsService {

    @Autowired
    private VideoStatsRepository videoStatsRepository;


    /**
     * 保存统计数据
     *
     * @param entity
     * @return
     */
    @Override
    public Boolean saveStats(VideoStats entity) {
        return videoStatsRepository.saveStats(entity);
    }

    /**
     * 批量查询统计数据
     *
     * @param idList
     * @return Map(id, entity)
     */
    @Override
    public QueryStatsDTO<VideoStats> getStats(List<Integer> idList) {
        Map<Integer, VideoStats> stats = videoStatsRepository.getStats(idList);
        return new QueryStatsDTO<>(stats);
    }
}
