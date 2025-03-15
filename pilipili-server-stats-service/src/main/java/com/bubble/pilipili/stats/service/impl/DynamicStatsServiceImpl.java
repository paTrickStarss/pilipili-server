/*
 * Copyright (c) 2025. Bubble
 */

package com.bubble.pilipili.stats.service.impl;

import com.bubble.pilipili.feign.pojo.dto.QueryStatsDTO;
import com.bubble.pilipili.feign.pojo.entity.DynamicStats;
import com.bubble.pilipili.stats.repository.DynamicStatsRepository;
import com.bubble.pilipili.stats.service.DynamicStatsService;
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

    /**
     * 保存统计数据
     *
     * @param entity
     * @return
     */
    @Override
    public Boolean saveStats(DynamicStats entity) {
        return dynamicStatsRepository.saveStats(entity);
    }

    /**
     * 批量查询统计数据
     *
     * @param idList
     * @return Map(id, entity)
     */
    @Override
    public QueryStatsDTO<DynamicStats> getStats(List<Integer> idList) {
        Map<Integer, DynamicStats> stats = dynamicStatsRepository.getStats(idList);
        return new QueryStatsDTO<>(stats);
    }
}
