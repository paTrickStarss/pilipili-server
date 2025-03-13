/*
 * Copyright (c) 2025. Bubble
 */

package com.bubble.pilipili.common.repository;

import com.bubble.pilipili.common.pojo.StatsEntity;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * 实体统计数据通用Repo
 * @author Bubble
 * @date 2025.03.09 17:57
 */
@Repository
public interface StatsEntityRepository<T extends StatsEntity> {

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
    Map<Integer, T> getStats(List<Integer> idList);
}
