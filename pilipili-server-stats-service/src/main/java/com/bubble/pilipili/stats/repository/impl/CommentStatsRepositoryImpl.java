/*
 * Copyright (c) 2025. Bubble
 */

package com.bubble.pilipili.stats.repository.impl;

import com.bubble.pilipili.common.repository.impl.CommonRepository;
import com.bubble.pilipili.common.pojo.CommentStats;
import com.bubble.pilipili.stats.mapper.CommentStatsMapper;
import com.bubble.pilipili.stats.repository.CommentStatsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * @author Bubble
 * @date 2025.03.09 18:02
 */
@Component
public class CommentStatsRepositoryImpl implements CommentStatsRepository {

    @Autowired
    private CommentStatsMapper commentStatsMapper;
    @Autowired
    private CommonRepository commonRepository;

    /**
     * 保存统计数据
     *
     * @param entity
     * @return
     */
    @Override
    public Boolean saveStats(CommentStats entity) {
//        return commentStatsMapper.insertOrUpdate(entity);
        return commonRepository.saveStats(
                entity,
                commentStatsMapper,
                CommentStats::getCid,
                CommentStats::getFavorCount,
                CommentStats::getDewCount
        );
    }

    /**
     * 批量查询统计数据
     *
     * @param idList
     * @return Map(id, entity)
     */
    @Override
    public Map<Integer, CommentStats> getStats(List<Integer> idList) {
        return commonRepository.getStats(idList, CommentStats::getCid, commentStatsMapper);
    }
}
