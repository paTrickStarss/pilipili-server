/*
 * Copyright (c) 2025. Bubble
 */

package com.bubble.pilipili.stats.service.impl;

import com.bubble.pilipili.common.constant.RedisKey;
import com.bubble.pilipili.feign.pojo.dto.QueryStatsDTO;
import com.bubble.pilipili.common.pojo.VideoStats;
import com.bubble.pilipili.stats.repository.VideoStatsRepository;
import com.bubble.pilipili.stats.service.VideoStatsService;
import com.bubble.pilipili.stats.util.StatsRedisHelper;
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
    @Autowired
    private StatsRedisHelper statsRedisHelper;


    /**
     * 保存统计数据
     *
     * @param entity
     * @return
     */
    @Override
    public Boolean saveStats(VideoStats entity) {
        Boolean b = videoStatsRepository.saveStats(entity);
        if (b) {
            // 保存成功，清除缓存
            statsRedisHelper.removeCache(RedisKey.VIDEO_STATS, entity.getVid());
        }
        return b;
    }

    /**
     * 批量查询统计数据
     *
     * @param idList
     * @return Map(id, entity)
     */
    @Override
    public QueryStatsDTO<VideoStats> getStats(List<Integer> idList) {
//        Map<Integer, VideoStats> stats = videoStatsRepository.getStats(idList);
        Map<Integer, VideoStats> statsMap = statsRedisHelper.queryStatsMapViaCache(
                idList,
                RedisKey.VIDEO_STATS,
                videoStatsRepository::getStats,
                VideoStats.class
        );
        return new QueryStatsDTO<>(statsMap);
    }
}
