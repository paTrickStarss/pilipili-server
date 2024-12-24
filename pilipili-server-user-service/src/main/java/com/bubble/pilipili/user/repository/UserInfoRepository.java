package com.bubble.pilipili.user.repository;

import com.bubble.pilipili.user.pojo.entity.UserInfo;

import java.util.List;

/**
 * @author liweixin@hcrc1.wecom.work
 * @date 2024/10/23
 */

public interface UserInfoRepository {

    Boolean saveUserInfo(UserInfo userInfo);

    Boolean updateUserInfo(UserInfo userInfo);

    UserInfo findUserInfoByUid(Integer uid);

    List<UserInfo> listUserInfo();
}
