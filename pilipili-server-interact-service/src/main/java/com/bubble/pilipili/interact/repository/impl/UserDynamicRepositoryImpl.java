/*
 * Copyright (c) 2025. Bubble
 */

package com.bubble.pilipili.interact.repository.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.bubble.pilipili.common.repository.impl.CommonRepository;
import com.bubble.pilipili.interact.mapper.UserDynamicMapper;
import com.bubble.pilipili.interact.pojo.entity.UserDynamic;
import com.bubble.pilipili.interact.repository.UserDynamicRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author Bubble
 * @date 2025.03.04 14:08
 */
@Component
public class UserDynamicRepositoryImpl implements UserDynamicRepository {

    @Autowired
    private UserDynamicMapper userDynamicMapper;
    @Autowired
    private CommonRepository commonRepository;

    /**
     * 保存互动数据
     *
     * @param interactEntity
     * @return
     */
    @Override
    public Boolean saveInteract(UserDynamic interactEntity) {
        return commonRepository.saveInteract(
                interactEntity,
                UserDynamic::getDid,
                UserDynamic::getUid,
                (updateWrapper, entity) -> {
                    if (entity.getFavor() != null) {
                        updateWrapper.set(UserDynamic::getFavor, entity.getFavor());
                        updateWrapper.ne(UserDynamic::getFavor, entity.getFavor());
                    }
                    if (entity.getRepost() != null) {
                        updateWrapper.set(UserDynamic::getRepost, entity.getRepost());
                        updateWrapper.ne(UserDynamic::getRepost, entity.getRepost());
                    }
                },
                userDynamicMapper
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
    public UserDynamic getInteract(Integer uid, Integer id) {
        return userDynamicMapper.selectOne(
                new LambdaQueryWrapper<UserDynamic>()
                        .eq(UserDynamic::getDid, id)
                        .eq(UserDynamic::getUid, uid)
        );
    }
}
