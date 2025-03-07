/*
 * Copyright (c) 2024-2025. Bubble
 */

package com.bubble.pilipili.user.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bubble.pilipili.common.pojo.PageDTO;
import com.bubble.pilipili.user.pojo.converter.UserInfoConverter;
import com.bubble.pilipili.user.pojo.dto.QueryFollowUserInfoDTO;
import com.bubble.pilipili.user.pojo.dto.QueryUserInfoDTO;
import com.bubble.pilipili.user.pojo.dto.QueryUserStatsDTO;
import com.bubble.pilipili.user.pojo.dto.SaveUserInfoDTO;
import com.bubble.pilipili.user.pojo.entity.UserInfo;
import com.bubble.pilipili.user.pojo.entity.UserRela;
import com.bubble.pilipili.user.pojo.req.PageQueryUserInfoReq;
import com.bubble.pilipili.user.pojo.req.SaveUserInfoReq;
import com.bubble.pilipili.user.repository.UserInfoRepository;
import com.bubble.pilipili.user.repository.UserRelaRepository;
import com.bubble.pilipili.user.service.UserInfoService;
import com.bubble.pilipili.user.util.UserInfoUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 用户信息业务类
 * @author liweixin@hcrc1.wecom.work
 * @date 2024/10/23
 */
@Slf4j
@Service
public class UserInfoServiceImpl implements UserInfoService {

    @Autowired
    private UserInfoRepository userInfoRepository;
    @Autowired
    private UserRelaRepository userRelaRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * 保存用户信息
     *
     * @param saveUserInfoReq
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public SaveUserInfoDTO saveUserInfo(SaveUserInfoReq saveUserInfoReq) {
        UserInfo userInfo = UserInfoConverter.getInstance().copyFieldValue(saveUserInfoReq, UserInfo.class);

        userInfo.setUuid(UUID.randomUUID().toString());
        Boolean saveSuccess = Boolean.FALSE;
        SaveUserInfoDTO dto = new SaveUserInfoDTO();
        try {
            // 加密password字段
            String encodedPassword = passwordEncoder.encode(userInfo.getPassword());
            userInfo.setPassword(encodedPassword);

            saveSuccess = userInfoRepository.saveUserInfo(userInfo);

        } catch (Exception e) {
            log.error("saveUserInfo error, {}", e.getMessage(), e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        } finally {
            dto.setSuccess(saveSuccess);
            dto.setUid(saveSuccess? userInfo.getUid() : null);
        }

        return dto;
    }

    /**
     * 更新用户信息
     *
     * @param saveUserInfoReq
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public SaveUserInfoDTO updateUserInfo(SaveUserInfoReq saveUserInfoReq) {
        UserInfo userInfo = UserInfoConverter.getInstance().copyFieldValue(saveUserInfoReq, UserInfo.class);
        Boolean success = userInfoRepository.updateUserInfo(userInfo);
        return new SaveUserInfoDTO(success? Boolean.TRUE:Boolean.FALSE, null);
    }

    /**
     * 根据UID查询用户信息
     *
     * @param uid
     * @return
     */
    @Override
    public QueryUserInfoDTO getUserInfoByUid(Integer uid) {
        UserInfo userInfo = userInfoRepository.findUserInfoByUid(uid);
        QueryUserInfoDTO dto = UserInfoConverter.getInstance().copyFieldValue(userInfo, QueryUserInfoDTO.class);
        dto.setLevel(UserInfoUtil.getLevel(dto.getExp()));

        // 获取关注粉丝数据
        QueryUserStatsDTO stats = getUserStats(Collections.singletonList(uid)).get(uid);
        dto.setFollowerCount(stats.getFollowerCount());
        dto.setFansCount(stats.getFansCount());

        return dto;
    }

    /**
     * 分页查询关注用户
     *
     * @param req
     * @return
     */
    @Override
    public PageDTO<QueryFollowUserInfoDTO> pageQueryFollowers(PageQueryUserInfoReq req) {
        Boolean special = req.getSpecial();
        if (special == null) { special = Boolean.FALSE; }

        Page<UserRela> userRelaPage = userRelaRepository.pageQueryUserRelaByFromUid(
                req.getUid(), special, req.getPageNo(), req.getPageSize());

        return handleUserRela(userRelaPage, req.getUid(), true);
    }

    /**
     * 分页查询粉丝用户
     *
     * @param req
     * @return
     */
    @Override
    public PageDTO<QueryFollowUserInfoDTO> pageQueryFans(PageQueryUserInfoReq req) {
        Page<UserRela> userRelaPage = userRelaRepository.pageQueryUserRelaByToUid(
                req.getUid(), req.getPageNo(), req.getPageSize());

        return handleUserRela(userRelaPage, req.getUid(), false);
    }

    /**
     * 关注用户
     *
     * @param fromUid 关注者uid
     * @param toUid 被关注者uid
     * @param special 特别关注
     * @return
     */
    @Override
    public Boolean followUser(Integer fromUid, Integer toUid, boolean special) {
        UserRela userRela = new UserRela();
        userRela.setFromUid(fromUid);
        userRela.setToUid(toUid);
        userRela.setSpecial(special? 1:0);
        return userRelaRepository.saveUserRela(userRela);
    }

    /**
     * 取消关注用户
     *
     * @param fromUid
     * @param toUid
     * @return
     */
    @Override
    public Boolean unfollowUser(Integer fromUid, Integer toUid) {
        return userRelaRepository.deleteUserRela(fromUid, toUid);
    }

    /**
     * 查询所有用户信息
     *
     * @return
     */
    @Override
    public List<QueryUserInfoDTO> listUserInfo() {
        List<UserInfo> userInfoList = userInfoRepository.listUserInfo();
        List<QueryUserInfoDTO> queryUserInfoDTOList = new ArrayList<>();
        for (UserInfo userInfo : userInfoList) {
            QueryUserInfoDTO queryUserInfoDTO = UserInfoConverter.getInstance().copyFieldValue(userInfo, QueryUserInfoDTO.class);
            queryUserInfoDTOList.add(queryUserInfoDTO);
        }

        return queryUserInfoDTOList;
    }


    /**
     * 批量查询用户的关注粉丝数
     * @param uidList
     * @return
     */
    private Map<Integer, QueryUserStatsDTO> getUserStats(List<Integer> uidList) {
        // 查询关注数
        Map<Long, Long> followerCountMap = userRelaRepository.countUserRela(uidList, false);

        // 查询粉丝数
        Map<Long, Long> fansCountMap = userRelaRepository.countUserRela(uidList, true);

        Map<Integer, QueryUserStatsDTO> resultMap = new HashMap<>();
        uidList.stream()
                .map(Long::new)
                .forEach(uid -> {
                    QueryUserStatsDTO dto = new QueryUserStatsDTO();
                    int uidInt = Math.toIntExact(uid);

                    dto.setUid(uidInt);
                    if (followerCountMap.containsKey(uid)) {
                        dto.setFollowerCount(followerCountMap.get(uid));
                    }
                    if (fansCountMap.containsKey(uid)) {
                        dto.setFansCount(fansCountMap.get(uid));
                    }
                    resultMap.put(uidInt, dto);
                });
        return resultMap;
    }

    private PageDTO<QueryFollowUserInfoDTO> handleUserRela(Page<UserRela> userRelaPage, Integer originUid, boolean follow) {
        Map<String, UserRela> userRelaMap = userRelaPage.getRecords()
                .stream()
                .collect(Collectors.toMap(
                        u -> String.join("->", u.getFromUid().toString(), u.getToUid().toString()),
                        Function.identity()
                ));

        List<Integer> uidList = userRelaPage.getRecords()
                .stream()
                .map(UserRela::getToUid)
                .collect(Collectors.toList());

        // 查询用户信息
        List<UserInfo> userInfoList = userInfoRepository.findUserInfoByUid(uidList);

        // 查询关注粉丝数据
        Map<Integer, QueryUserStatsDTO> userStatsMap = getUserStats(uidList);

        // 装填dto
        List<QueryFollowUserInfoDTO> dtoList = userInfoList.stream()
                .map(userinfo -> {
                    QueryFollowUserInfoDTO dto =
                            UserInfoConverter.getInstance().copyFieldValue(
                                    userinfo, QueryFollowUserInfoDTO.class);
                    QueryUserStatsDTO stats = userStatsMap.get(userinfo.getUid());
                    dto.setFollowerCount(stats.getFollowerCount());
                    dto.setFansCount(stats.getFansCount());
                    dto.setLevel(UserInfoUtil.getLevel(userinfo.getExp()));

                    String fromUid = follow? originUid.toString(): userinfo.getUid().toString();
                    String toUid = follow? userinfo.getUid().toString(): originUid.toString();
                    Integer special = userRelaMap.get(String.join("->", fromUid, toUid)).getSpecial();
                    dto.setSpecial(special == 1);
                    return dto;
                })
                .collect(Collectors.toList());

        PageDTO<QueryFollowUserInfoDTO> dto = new PageDTO<>();
        dto.setPageNo(userRelaPage.getCurrent());
        dto.setPageSize(userRelaPage.getSize());
        dto.setTotal(userRelaPage.getTotal());
        dto.setData(dtoList);
        return dto;
    }
}
