/*
 * Copyright (c) 2025. Bubble
 */

package com.bubble.pilipili.stats.service.impl;

import com.bubble.pilipili.common.constant.RedisKey;
import com.bubble.pilipili.feign.pojo.dto.QueryStatsDTO;
import com.bubble.pilipili.common.pojo.DynamicStats;
import com.bubble.pilipili.stats.repository.DynamicStatsRepository;
import com.bubble.pilipili.stats.service.DynamicStatsService;
import com.bubble.pilipili.stats.util.StatsRedisHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author Bubble
 * @date 2025.03.15 22:43
 */
@Service
public class DynamicStatsServiceImpl implements DynamicStatsService {

    @Autowired
    private DynamicStatsRepository dynamicStatsRepository;
    @Autowired
    private StatsRedisHelper statsRedisHelper;

    /**
     * 保存统计数据
     *
     * @param entity
     * @return
     */
    @Override
    public Boolean saveStats(DynamicStats entity) {
        Boolean b = dynamicStatsRepository.saveStats(entity);
        if (b) {
            statsRedisHelper.removeCache(RedisKey.DYNAMIC_STATS, entity.getDid());
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
    public QueryStatsDTO<DynamicStats> getStats(List<Integer> idList) {
//        Map<Integer, DynamicStats> stats = dynamicStatsRepository.getStats(idList);
        Map<Integer, DynamicStats> statsMap = statsRedisHelper.queryStatsMapViaCache(
                idList,
                RedisKey.DYNAMIC_STATS,
                dynamicStatsRepository::getStats,
                DynamicStats.class
        );
        return new QueryStatsDTO<>(statsMap);
    }
}
