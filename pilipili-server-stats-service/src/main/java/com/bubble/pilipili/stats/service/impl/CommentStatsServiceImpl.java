/*
 * Copyright (c) 2025. Bubble
 */

package com.bubble.pilipili.stats.service.impl;

import com.bubble.pilipili.feign.pojo.dto.QueryStatsDTO;
import com.bubble.pilipili.common.pojo.CommentStats;
import com.bubble.pilipili.stats.repository.CommentStatsRepository;
import com.bubble.pilipili.stats.service.CommentStatsService;
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

    /**
     * 保存统计数据
     *
     * @param entity
     * @return
     */
    @Override
    public Boolean saveStats(CommentStats entity) {
        return commentStatsRepository.saveStats(entity);
    }

    /**
     * 批量查询统计数据
     *
     * @param idList
     * @return Map(id, entity)
     */
    @Override
    public QueryStatsDTO<CommentStats> getStats(List<Integer> idList) {
        Map<Integer, CommentStats> stats = commentStatsRepository.getStats(idList);
        return new QueryStatsDTO<>(stats);
    }
}
