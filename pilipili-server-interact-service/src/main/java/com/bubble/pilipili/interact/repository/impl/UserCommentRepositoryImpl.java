/*
 * Copyright (c) 2025. Bubble
 */

package com.bubble.pilipili.interact.repository.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.bubble.pilipili.common.repository.impl.CommonRepository;
import com.bubble.pilipili.interact.mapper.UserCommentMapper;
import com.bubble.pilipili.interact.pojo.entity.UserComment;
import com.bubble.pilipili.interact.repository.UserCommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author Bubble
 * @date 2025.03.05 21:30
 */
@Component
public class UserCommentRepositoryImpl implements UserCommentRepository {

    @Autowired
    private UserCommentMapper userCommentMapper;
    @Autowired
    private CommonRepository commonRepository;

    /**
     * 保存互动数据
     *
     * @param interactEntity
     * @return
     */
    @Override
    public Boolean saveInteract(UserComment interactEntity) {
        return commonRepository.saveInteract(
                interactEntity,
                UserComment::getCid,
                UserComment::getUid,
                (updateWrapper, entity) -> {
                    if (entity.getFavor() != null) {
                        updateWrapper.set(UserComment::getFavor, entity.getFavor());
                        updateWrapper.ne(UserComment::getFavor, entity.getFavor());
                    }
                    if (entity.getDew() != null) {
                        updateWrapper.set(UserComment::getDew, entity.getDew());
                        updateWrapper.ne(UserComment::getDew, entity.getDew());
                    }
                },
                userCommentMapper
        );
    }

    /**
     * 查询互动数据
     *
     * @param uid
     * @param id
     * @return
     */
    @Override
    public UserComment getInteract(Integer uid, Integer id) {
        return userCommentMapper.selectOne(
                new LambdaQueryWrapper<UserComment>()
                        .eq(UserComment::getCid, id)
                        .eq(UserComment::getUid, uid)
        );
    }

}
