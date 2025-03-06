/*
 * Copyright (c) 2024-2025. Bubble
 */

package com.bubble.pilipili.user.service;

import com.bubble.pilipili.user.pojo.dto.QueryUserInfoDTO;
import com.bubble.pilipili.user.pojo.dto.SaveUserInfoDTO;
import com.bubble.pilipili.user.pojo.req.SaveUserInfoReq;

import java.util.List;

/**
 * @author liweixin@hcrc1.wecom.work
 * @date 2024/10/23
 */
public interface UserInfoService {

    /**
     * 保存用户信息
     * @param saveUserInfoReq
     * @return
     */
    SaveUserInfoDTO saveUserInfo(SaveUserInfoReq saveUserInfoReq);

    /**
     * 更新用户信息
     * @param saveUserInfoReq
     * @return
     */
    SaveUserInfoDTO updateUserInfo(SaveUserInfoReq saveUserInfoReq);

    /**
     * 根据UID查询用户信息
     * @param uid
     * @return
     */
    QueryUserInfoDTO getUserInfoByUid(String uid);

    /**
     * 查询所有用户信息
     * @return
     */
    List<QueryUserInfoDTO> listUserInfo();

}
