/*
 * Copyright (c) 2024-2025. Bubble
 */

package com.bubble.pilipili.user.repository.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.bubble.pilipili.common.util.ListUtil;
import com.bubble.pilipili.user.mapper.UserInfoMapper;
import com.bubble.pilipili.user.pojo.entity.UserInfo;
import com.bubble.pilipili.user.repository.UserInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

/**
 * @author liweixin@hcrc1.wecom.work
 * @date 2024/10/23
 */
@Component()
public class UserInfoRepositoryImpl implements UserInfoRepository {

    @Autowired
    private UserInfoMapper userInfoMapper;

    @Override
    public Boolean saveUserInfo(UserInfo userInfo) {
        int rows = userInfoMapper.insert(userInfo);
        return rows == 1;
    }

    @Override
    public Boolean updateUserInfo(UserInfo userInfo) {
        int rows = userInfoMapper.updateById(userInfo);
        return rows == 1;
    }

    @Override
    public UserInfo findUserInfoByUid(Integer uid) {
//        return userInfoMapper.selectOne(new QueryWrapper<UserInfo>().eq("uid", uid));
        return userInfoMapper.selectById(uid);
    }

    /**
     * 批量查询用户信息
     * @param uidList
     * @return
     */
    @Override
    public List<UserInfo> findUserInfoByUid(List<Integer> uidList) {
        if (ListUtil.isEmpty(uidList)) {
            return Collections.emptyList();
        }

        return userInfoMapper.selectList(
                new LambdaQueryWrapper<UserInfo>()
                        .in(UserInfo::getUid, uidList)
        );
    }

    @Override
    public List<UserInfo> listUserInfo() {
        return userInfoMapper.selectList(null);
    }
}
