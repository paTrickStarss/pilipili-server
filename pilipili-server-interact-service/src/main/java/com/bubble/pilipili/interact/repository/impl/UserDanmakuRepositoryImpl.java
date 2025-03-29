/*
 * Copyright (c) 2025. Bubble
 */

package com.bubble.pilipili.interact.repository.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.bubble.pilipili.common.repository.impl.CommonRepository;
import com.bubble.pilipili.interact.mapper.UserDanmakuMapper;
import com.bubble.pilipili.interact.pojo.entity.UserDanmaku;
import com.bubble.pilipili.interact.repository.UserDanmakuRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author Bubble
 * @date 2025.03.06 14:39
 */
@Component
public class UserDanmakuRepositoryImpl implements UserDanmakuRepository {

    @Autowired
    private UserDanmakuMapper userDanmakuMapper;
    @Autowired
    private CommonRepository commonRepository;

    /**
     * 保存互动数据
     *
     * @param interactEntity
     * @return
     */
    @Override
    public Boolean saveInteract(UserDanmaku interactEntity) {
        return commonRepository.saveInteract(
                interactEntity,
                UserDanmaku::getDanmakuId,
                UserDanmaku::getUid,
                (updateWrapper, entity) -> {
                    if (entity.getFavor() != null) {
                        updateWrapper.set(UserDanmaku::getFavor, entity.getFavor());
                        updateWrapper.ne(UserDanmaku::getFavor, entity.getFavor());
                    }
                    if (entity.getDew() != null) {
                        updateWrapper.set(UserDanmaku::getDew, entity.getDew());
                        updateWrapper.ne(UserDanmaku::getDew, entity.getDew());
                    }
                },
                userDanmakuMapper
        );
    }

    /**
     * 查询互动数据
     *
     * @param id
     * @param uid
     * @return
     */
    @Override
    public UserDanmaku getInteract(Integer id, Integer uid) {
        return userDanmakuMapper.selectOne(
                new LambdaQueryWrapper<UserDanmaku>()
                        .eq(UserDanmaku::getDanmakuId, id)
                        .eq(UserDanmaku::getUid, uid)
        );
    }
}
