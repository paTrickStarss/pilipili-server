/*
 * Copyright (c) 2025. Bubble
 */

package com.bubble.pilipili.interact.repository.impl;

import com.bubble.pilipili.common.repository.impl.CommonRepoImpl;
import com.bubble.pilipili.interact.mapper.DanmakuStatsMapper;
import com.bubble.pilipili.interact.pojo.entity.DanmakuStats;
import com.bubble.pilipili.interact.repository.DanmakuStatsRepository;
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

    /**
     * 保存统计数据
     *
     * @param entity
     * @return
     */
    @Override
    public Boolean saveStats(DanmakuStats entity) {
        return danmakuStatsMapper.insertOrUpdate(entity);
    }

    /**
     * 批量查询统计数据
     *
     * @param idList
     * @return Map(id, entity)
     */
    @Override
    public Map<Integer, DanmakuStats> getStats(List<Integer> idList) {
        return CommonRepoImpl.getStats(idList, DanmakuStats::getDanmakuId, danmakuStatsMapper);
    }
}
