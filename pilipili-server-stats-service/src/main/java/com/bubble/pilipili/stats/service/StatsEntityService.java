/*
 * Copyright (c) 2025. Bubble
 */

package com.bubble.pilipili.stats.service;

import com.bubble.pilipili.common.pojo.StatsEntity;
import com.bubble.pilipili.feign.pojo.dto.QueryStatsDTO;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Bubble
 * @date 2025.03.15 20:24
 */
@Service
public interface StatsEntityService<T extends StatsEntity> {

    /**
     * 保存统计数据
     * @param entity
     * @return
     */
    Boolean saveStats(T entity);

    /**
     * 批量查询统计数据
     * @param idList
     * @return Map(id, entity)
     */
    QueryStatsDTO<T> getStats(List<Integer> idList);
}
