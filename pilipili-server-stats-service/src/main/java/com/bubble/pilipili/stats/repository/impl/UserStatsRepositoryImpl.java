/*
 * Copyright (c) 2025. Bubble
 */

package com.bubble.pilipili.stats.repository.impl;

import com.bubble.pilipili.common.repository.impl.CommonRepository;
import com.bubble.pilipili.common.pojo.UserStats;
import com.bubble.pilipili.stats.mapper.UserStatsMapper;
import com.bubble.pilipili.stats.repository.UserStatsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @author Bubble
 * @date 2025.03.19 23:03
 */
@Repository
public class UserStatsRepositoryImpl implements UserStatsRepository {

    @Autowired
    private UserStatsMapper userStatsMapper;
    @Autowired
    private CommonRepository commonRepository;

    /**
     * 保存统计数据
     *
     * @param entity
     * @return
     */
    @Override
    public Boolean saveStats(UserStats entity) {
        return commonRepository.saveStats(
                entity,
                userStatsMapper,
                UserStats::getUid,
                UserStats::getFollowerCount,
                UserStats::getFansCount,
                UserStats::getDynamicCount
        );
    }

    /**
     * 批量查询统计数据
     *
     * @param idList
     * @return Map(id, entity)
     */
    @Override
    public Map<Integer, UserStats> getStats(List<Integer> idList) {
        return commonRepository.getStats(idList, UserStats::getUid, userStatsMapper);
    }
}
