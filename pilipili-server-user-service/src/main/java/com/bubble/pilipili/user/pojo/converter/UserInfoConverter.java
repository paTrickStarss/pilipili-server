/*
 * Copyright (c) 2024. Bubble
 */

package com.bubble.pilipili.user.pojo.converter;

import com.bubble.pilipili.common.pojo.converter.BaseConverter;
import com.bubble.pilipili.user.pojo.entity.UserInfo;
import com.bubble.pilipili.user.pojo.req.SaveUserInfoReq;
import com.bubble.pilipili.user.pojo.req.UpdateUserInfoReq;

/**
 * @author liweixin@hcrc1.wecom.work
 * @date 2024/10/23
 */
public class UserInfoConverter extends BaseConverter {

    public UserInfo fromSaveRequest(SaveUserInfoReq saveUserInfoReq) {
        UserInfo userInfo = new UserInfo();
        userInfo.setNickname(saveUserInfoReq.getNickname());
        userInfo.setPassword(saveUserInfoReq.getPassword());
        userInfo.setGender(saveUserInfoReq.getGender());
        userInfo.setAvatarUrl(saveUserInfoReq.getAvatarUrl());
        userInfo.setEmail(saveUserInfoReq.getEmail());

        return userInfo;
    }

    public UserInfo fromUpdateRequest(UpdateUserInfoReq updateUserInfoReq) {
        UserInfo userInfo = new UserInfo();
        userInfo.setUid(updateUserInfoReq.getUid());
        userInfo.setUuid(updateUserInfoReq.getUuid());
        userInfo.setNickname(updateUserInfoReq.getNickname());

        return userInfo;
    }
}
