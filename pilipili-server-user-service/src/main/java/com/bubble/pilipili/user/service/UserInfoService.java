/*
 * Copyright (c) 2024. Bubble
 */

package com.bubble.pilipili.user.service;

import com.bubble.pilipili.user.pojo.dto.QueryUserInfoDTO;
import com.bubble.pilipili.user.pojo.dto.SaveUserInfoDTO;
import com.bubble.pilipili.user.pojo.req.SaveUserInfoRequest;
import com.bubble.pilipili.user.pojo.req.UpdateUserInfoRequest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author liweixin@hcrc1.wecom.work
 * @date 2024/10/23
 */
public interface UserInfoService {

    /**
     * 保存用户信息
     * @param saveUserInfoRequest
     * @return
     */
    SaveUserInfoDTO saveUserInfo(SaveUserInfoRequest saveUserInfoRequest);

    /**
     * 更新用户信息
     * @param saveUserInfoRequest
     * @return
     */
    SaveUserInfoDTO updateUserInfo(SaveUserInfoRequest saveUserInfoRequest);

    /**
     * 根据UID查询用户信息
     * @param uid
     * @return
     */
    QueryUserInfoDTO getUserInfoByUid(Integer uid);

    /**
     * 查询所有用户信息
     * @return
     */
    List<QueryUserInfoDTO> listUserInfo();

    /**
     * 注销用户
     * @param saveUserInfoRequest
     * @return
     */
    SaveUserInfoDTO invalidateUser(SaveUserInfoRequest saveUserInfoRequest);

    /**
     * 封禁用户
     * @param saveUserInfoRequest
     * @return
     */
    SaveUserInfoDTO blockUser(SaveUserInfoRequest saveUserInfoRequest);
}
