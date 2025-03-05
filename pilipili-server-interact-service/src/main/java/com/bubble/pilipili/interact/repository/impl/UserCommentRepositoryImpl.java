/*
 * Copyright (c) 2025. Bubble
 */

package com.bubble.pilipili.interact.repository.impl;

import com.bubble.pilipili.interact.mapper.UserCommentMapper;
import com.bubble.pilipili.interact.pojo.dto.QueryCommentStatsDTO;
import com.bubble.pilipili.interact.pojo.entity.UserComment;
import com.bubble.pilipili.interact.repository.UserCommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

/**
 * @author Bubble
 * @date 2025.03.05 21:30
 */
@Component
public class UserCommentRepositoryImpl implements UserCommentRepository {

    @Autowired
    private UserCommentMapper userCommentMapper;

    /**
     * @param userComment
     * @return
     */
    @Override
    public Boolean saveUserComment(UserComment userComment) {
        return CommonRepoImpl.save(
                userComment,
                UserComment::getCid,
                UserComment::getUid,
                (updateWrapper, entity) -> {
                    if (entity.getFavor() != null) {
                        updateWrapper.set(UserComment::getFavor, entity.getFavor());
                    }
                    if (entity.getDew() != null) {
                        updateWrapper.set(UserComment::getDew, entity.getDew());
                    }
                },
                userCommentMapper
        );
    }

    /**
     * @param cid
     * @return
     */
    @Override
    public QueryCommentStatsDTO getCommentStats(Integer cid) {
        return CommonRepoImpl.getStatsBatch(
                Collections.singletonList(cid),
                QueryCommentStatsDTO.class,
                (entity, dto) -> {
                    dto.setCid(entity.getCid());
                    dto.setFavorCount(entity.getFavor());
                    dto.setDewCount(entity.getDew());
                },
                userCommentMapper,
                "cid",
                "favor", "dew"
        ).get(0);
    }

    /**
     * @param cidList
     * @return
     */
    @Override
    public List<QueryCommentStatsDTO> getCommentStats(List<Integer> cidList) {
        return CommonRepoImpl.getStatsBatch(
                cidList,
                QueryCommentStatsDTO.class,
                (entity, dto) -> {
                    dto.setCid(entity.getCid());
                    dto.setFavorCount(entity.getFavor());
                    dto.setDewCount(entity.getDew());
                },
                userCommentMapper,
                "cid",
                "favor", "dew"
        );
    }
}
