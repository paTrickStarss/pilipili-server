/*
 * Copyright (c) 2025. Bubble
 */

package com.bubble.pilipili.stats.service.impl;

import com.bubble.pilipili.common.constant.RedisKey;
import com.bubble.pilipili.feign.pojo.dto.QueryStatsDTO;
import com.bubble.pilipili.common.pojo.CommentStats;
import com.bubble.pilipili.stats.repository.CommentStatsRepository;
import com.bubble.pilipili.stats.service.CommentStatsService;
import com.bubble.pilipili.stats.util.StatsRedisHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author Bubble
 * @date 2025.03.15 22:41
 */
@Service
public class CommentStatsServiceImpl implements CommentStatsService {

    @Autowired
    private CommentStatsRepository commentStatsRepository;
    @Autowired
    private StatsRedisHelper statsRedisHelper;

    /**
     * 保存统计数据
     *
     * @param entity
     * @return
     */
    @Override
    public Boolean saveStats(CommentStats entity) {
        Boolean b = commentStatsRepository.saveStats(entity);
        if (b) {
            statsRedisHelper.removeCache(RedisKey.COMMENT_STATS, entity.getCid());
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
    public QueryStatsDTO<CommentStats> getStats(List<Integer> idList) {
//        Map<Integer, CommentStats> stats = commentStatsRepository.getStats(idList);
        Map<Integer, CommentStats> statsMap = statsRedisHelper.queryStatsMapViaCache(
                idList,
                RedisKey.COMMENT_STATS,
                commentStatsRepository::getStats,
                CommentStats.class
        );
        return new QueryStatsDTO<>(statsMap);
    }
}
