/*
 * Copyright (c) 2025. Bubble
 */

package com.bubble.pilipili.stats.repository.impl;

import com.bubble.pilipili.common.repository.impl.CommonRepository;
import com.bubble.pilipili.common.pojo.DynamicStats;
import com.bubble.pilipili.stats.mapper.DynamicStatsMapper;
import com.bubble.pilipili.stats.repository.DynamicStatsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * @author Bubble
 * @date 2025.03.09 20:52
 */
@Component
public class DynamicStatsRepositoryImpl implements DynamicStatsRepository {

    @Autowired
    private DynamicStatsMapper dynamicStatsMapper;
    @Autowired
    private CommonRepository commonRepository;

    /**
     * 保存统计数据
     *
     * @param entity
     * @return
     */
    @Override
    public Boolean saveStats(DynamicStats entity) {
//        return dynamicStatsMapper.insertOrUpdate(commentStats);
        return commonRepository.saveStats(
                entity,
                dynamicStatsMapper,
                DynamicStats::getDid,
                DynamicStats::getFavorCount,
                DynamicStats::getCommentCount,
                DynamicStats::getRepostCount
        );
    }

    /**
     * 批量查询统计数据
     *
     * @param idList
     * @return Map(id, entity)
     */
    @Override
    public Map<Integer, DynamicStats> getStats(List<Integer> idList) {
        return commonRepository.getStats(idList, DynamicStats::getDid, dynamicStatsMapper);
    }
}
