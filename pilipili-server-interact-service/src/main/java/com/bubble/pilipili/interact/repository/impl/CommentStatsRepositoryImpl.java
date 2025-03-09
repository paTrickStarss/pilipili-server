/*
 * Copyright (c) 2025. Bubble
 */

package com.bubble.pilipili.interact.repository.impl;

import com.bubble.pilipili.common.repository.impl.CommonRepoImpl;
import com.bubble.pilipili.interact.mapper.CommentStatsMapper;
import com.bubble.pilipili.interact.pojo.entity.CommentStats;
import com.bubble.pilipili.interact.repository.CommentStatsRepository;
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

    /**
     * 保存统计数据
     *
     * @param entity
     * @return
     */
    @Override
    public Boolean saveStats(CommentStats entity) {
        return commentStatsMapper.insertOrUpdate(entity);
    }

    /**
     * 批量查询统计数据
     *
     * @param idList
     * @return Map(id, entity)
     */
    @Override
    public Map<Integer, CommentStats> getStats(List<Integer> idList) {
        return CommonRepoImpl.getStats(idList, CommentStats::getCid, commentStatsMapper);
    }
}
