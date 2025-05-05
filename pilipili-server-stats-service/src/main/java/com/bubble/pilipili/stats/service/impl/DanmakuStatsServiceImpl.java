/*
 * Copyright (c) 2025. Bubble
 */

package com.bubble.pilipili.stats.service.impl;

import com.bubble.pilipili.feign.pojo.dto.QueryStatsDTO;
import com.bubble.pilipili.common.pojo.DanmakuStats;
import com.bubble.pilipili.stats.repository.DanmakuStatsRepository;
import com.bubble.pilipili.stats.service.DanmakuStatsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author Bubble
 * @date 2025.03.15 22:42
 */
@Service
public class DanmakuStatsServiceImpl implements DanmakuStatsService {

    @Autowired
    private DanmakuStatsRepository danmakuStatsRepository;

    /**
     * 保存统计数据
     *
     * @param entity
     * @return
     */
    @Override
    public Boolean saveStats(DanmakuStats entity) {
        return danmakuStatsRepository.saveStats(entity);
    }

    /**
     * 批量查询统计数据
     *
     * @param idList
     * @return Map(id, entity)
     */
    @Override
    public QueryStatsDTO<DanmakuStats> getStats(List<Integer> idList) {
        Map<Integer, DanmakuStats> stats = danmakuStatsRepository.getStats(idList);
        return new QueryStatsDTO<>(stats);
    }
}
