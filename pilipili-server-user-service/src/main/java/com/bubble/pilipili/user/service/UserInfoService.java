/*
 * Copyright (c) 2024-2025. Bubble
 */

package com.bubble.pilipili.user.service;

import com.bubble.pilipili.common.pojo.PageDTO;
import com.bubble.pilipili.user.pojo.dto.QueryFollowUserInfoDTO;
import com.bubble.pilipili.feign.pojo.dto.QueryUserInfoDTO;
import com.bubble.pilipili.user.pojo.dto.QueryUserRelaDTO;
import com.bubble.pilipili.user.pojo.dto.SaveUserInfoDTO;
import com.bubble.pilipili.user.pojo.req.PageQueryUserInfoReq;
import com.bubble.pilipili.user.pojo.req.SaveUserInfoReq;

import java.util.List;
import java.util.Map;

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
    QueryUserInfoDTO getUserInfoByUid(Integer uid);
    /**
     * 批量查询用户信息
     * @param uidList
     * @return
     */
    Map<Integer, QueryUserInfoDTO> getUserInfoByUid(List<Integer> uidList);

    /**
     * 分页查询关注用户
     * @param req
     * @return
     */
    PageDTO<QueryFollowUserInfoDTO> pageQueryFollowers(PageQueryUserInfoReq req);

    /**
     * 分页查询粉丝用户
     * @param req
     * @return
     */
    PageDTO<QueryFollowUserInfoDTO> pageQueryFans(PageQueryUserInfoReq req);

    /**
     * 关注用户
     * @param fromUid 关注者uid
     * @param toUid 被关注者uid
     * @param special 特别关注
     * @return
     */
    Boolean followUser(Integer fromUid, Integer toUid, boolean special);

    /**
     * 取消关注用户
     * @param fromUid
     * @param toUid
     * @return
     */
    Boolean unfollowUser(Integer fromUid, Integer toUid);
    /**
     * 查询用户关系（需要交叉检查互粉情况）
     * @param fromUid
     * @param toUid
     * @return
     */
    QueryUserRelaDTO queryUserRela(Integer fromUid, Integer toUid);

    /**
     * 查询所有用户信息
     * @return
     */
    List<QueryUserInfoDTO> listUserInfo();

}
