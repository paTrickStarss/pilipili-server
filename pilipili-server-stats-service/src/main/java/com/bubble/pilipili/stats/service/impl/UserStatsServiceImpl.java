/*
 * Copyright (c) 2025. Bubble
 */

package com.bubble.pilipili.stats.service.impl;

import com.bubble.pilipili.feign.pojo.dto.QueryStatsDTO;
import com.bubble.pilipili.common.pojo.UserStats;
import com.bubble.pilipili.stats.repository.UserStatsRepository;
import com.bubble.pilipili.stats.service.UserStatsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author Bubble
 * @date 2025.03.19 23:05
 */
@Service
public class UserStatsServiceImpl implements UserStatsService {

    @Autowired
    private UserStatsRepository userStatsRepository;

    /**
     * 保存统计数据
     *
     * @param entity
     * @return
     */
    @Override
    public Boolean saveStats(UserStats entity) {
        return userStatsRepository.saveStats(entity);
    }

    /**
     * 批量查询统计数据
     *
     * @param idList
     * @return Map(id, entity)
     */
    @Override
    public QueryStatsDTO<UserStats> getStats(List<Integer> idList) {
        Map<Integer, UserStats> stats = userStatsRepository.getStats(idList);
        return new QueryStatsDTO<>(stats);
    }
}
