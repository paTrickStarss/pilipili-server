package com.bubble.pilipili.user.pojo.converter;

import com.bubble.pilipili.user.pojo.entity.UserInfo;
import com.bubble.pilipili.user.pojo.req.SaveUserInfoRequest;
import com.bubble.pilipili.user.pojo.req.UpdateUserInfoRequest;

/**
 * @author liweixin@hcrc1.wecom.work
 * @date 2024/10/23
 */
public class UserInfoConverter {

    public static UserInfo fromSaveRequest(SaveUserInfoRequest saveUserInfoRequest) {
        UserInfo userInfo = new UserInfo();
        userInfo.setNickname(saveUserInfoRequest.getNickname());
        userInfo.setPassword(saveUserInfoRequest.getPassword());
        userInfo.setGender(saveUserInfoRequest.getGender());
        userInfo.setAvatarUrl(saveUserInfoRequest.getAvatarUrl());
        userInfo.setEmail(saveUserInfoRequest.getEmail());

        return userInfo;
    }

    public static UserInfo fromUpdateRequest(UpdateUserInfoRequest updateUserInfoRequest) {
        UserInfo userInfo = new UserInfo();
        userInfo.setUid(updateUserInfoRequest.getUid());
        userInfo.setUuid(updateUserInfoRequest.getUuid());
        userInfo.setNickname(updateUserInfoRequest.getNickname());

        return userInfo;
    }
}
