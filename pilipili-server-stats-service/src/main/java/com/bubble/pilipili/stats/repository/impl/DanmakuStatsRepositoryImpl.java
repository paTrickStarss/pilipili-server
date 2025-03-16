/*
 * Copyright (c) 2025. Bubble
 */

package com.bubble.pilipili.stats.repository.impl;

import com.bubble.pilipili.common.repository.impl.CommonRepository;
import com.bubble.pilipili.feign.pojo.entity.DanmakuStats;
import com.bubble.pilipili.stats.mapper.DanmakuStatsMapper;
import com.bubble.pilipili.stats.repository.DanmakuStatsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * @author Bubble
 * @date 2025.03.09 20:35
 */
@Component
public class DanmakuStatsRepositoryImpl implements DanmakuStatsRepository {

    @Autowired
    private DanmakuStatsMapper danmakuStatsMapper;
    @Autowired
    private CommonRepository commonRepository;

    /**
     * 保存统计数据
     *
     * @param entity
     * @return
     */
    @Override
    public Boolean saveStats(DanmakuStats entity) {
//        return danmakuStatsMapper.insertOrUpdate(entity);
        return commonRepository.saveStats(
                entity,
                danmakuStatsMapper,
                DanmakuStats::getDanmakuId,
                DanmakuStats::getFavorCount,
                DanmakuStats::getDewCount
        );
    }

    /**
     * 批量查询统计数据
     *
     * @param idList
     * @return Map(id, entity)
     */
    @Override
    public Map<Integer, DanmakuStats> getStats(List<Integer> idList) {
        return commonRepository.getStats(idList, DanmakuStats::getDanmakuId, danmakuStatsMapper);
    }
}
