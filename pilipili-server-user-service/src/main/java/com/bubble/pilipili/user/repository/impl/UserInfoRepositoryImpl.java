package com.bubble.pilipili.user.repository.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.bubble.pilipili.user.mapper.UserInfoMapper;
import com.bubble.pilipili.user.pojo.entity.UserInfo;
import com.bubble.pilipili.user.repository.UserInfoRepository;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author liweixin@hcrc1.wecom.work
 * @date 2024/10/23
 */
@Component("UserInfoRepository")
public class UserInfoRepositoryImpl implements UserInfoRepository {

    @Resource
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
        return userInfoMapper.selectOne(new QueryWrapper<UserInfo>().eq("uid", uid));
    }

    @Override
    public List<UserInfo> listUserInfo() {
        return userInfoMapper.selectList(null);
    }
}
