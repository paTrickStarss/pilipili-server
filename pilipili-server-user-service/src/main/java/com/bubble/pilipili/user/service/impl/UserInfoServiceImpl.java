/*
 * Copyright (c) 2024-2025. Bubble
 */

package com.bubble.pilipili.user.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bubble.pilipili.common.component.EntityConverter;
import com.bubble.pilipili.common.constant.RedisKey;
import com.bubble.pilipili.common.exception.NotFountException;
import com.bubble.pilipili.common.exception.ServiceOperationException;
import com.bubble.pilipili.common.http.SimpleResponse;
import com.bubble.pilipili.common.pojo.PageDTO;
import com.bubble.pilipili.common.util.ListUtil;
import com.bubble.pilipili.feign.api.StatsFeignAPI;
import com.bubble.pilipili.feign.pojo.dto.QueryStatsDTO;
import com.bubble.pilipili.common.pojo.UserStats;
import com.bubble.pilipili.feign.pojo.dto.QueryUserInfoDTO;
import com.bubble.pilipili.user.pojo.dto.*;
import com.bubble.pilipili.user.pojo.entity.UserInfo;
import com.bubble.pilipili.user.pojo.entity.UserRela;
import com.bubble.pilipili.user.pojo.req.PageQueryUserInfoReq;
import com.bubble.pilipili.user.pojo.req.SaveUserInfoReq;
import com.bubble.pilipili.user.repository.UserInfoRepository;
import com.bubble.pilipili.user.repository.UserRelaRepository;
import com.bubble.pilipili.user.service.UserInfoService;
import com.bubble.pilipili.user.util.UserInfoUtil;
import com.bubble.pilipili.user.util.UserRedisHelper;
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
    @Autowired
    private StatsFeignAPI statsFeignAPI;
    
    @Autowired
    private EntityConverter entityConverter;
    @Autowired
    private UserRedisHelper userRedisHelper;

    /**
     * 保存用户信息
     *
     * @param saveUserInfoReq
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public SaveUserInfoDTO saveUserInfo(SaveUserInfoReq saveUserInfoReq) {
        UserInfo userInfo = entityConverter.copyFieldValue(saveUserInfoReq, UserInfo.class);

        Boolean saveSuccess = Boolean.FALSE;
        SaveUserInfoDTO dto = new SaveUserInfoDTO();
        try {
            // 加密password字段
            String encodedPassword = passwordEncoder.encode(userInfo.getPassword());
            userInfo.setPassword(encodedPassword);

            saveSuccess = userInfoRepository.saveUserInfo(userInfo);
            if (saveSuccess) {
                userRedisHelper.removeCache(RedisKey.USER_INFO, userInfo.getUid());
            }

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
        UserInfo userInfo = entityConverter.copyFieldValue(saveUserInfoReq, UserInfo.class);
        Boolean success = userInfoRepository.updateUserInfo(userInfo);
        if (success) {
            userRedisHelper.removeCache(RedisKey.USER_INFO, saveUserInfoReq.getUid());
        }
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
        UserInfo userInfo = userRedisHelper.queryViaCache(
                uid, RedisKey.USER_INFO,
                userInfoRepository::findUserInfoByUid,
                UserInfo.class
        );
        if (userInfo == null) {
            throw new NotFountException("用户不存在");
        }
        QueryUserInfoDTO dto = entityConverter.copyFieldValue(userInfo, QueryUserInfoDTO.class);
        dto.setLevel(UserInfoUtil.getLevel(dto.getExp()));

        // 获取关注粉丝数据
        UserStats stats = getUserStats(Collections.singletonList(uid)).get(uid);
        if (stats != null) {
            dto.setFollowerCount(stats.getFollowerCount());
            dto.setFansCount(stats.getFansCount());
            dto.setDynamicCount(stats.getDynamicCount());
        }

        return dto;
    }

    /**
     * 批量查询用户信息
     *
     * @param uidList
     * @return
     */
    @Override
    public Map<Integer, QueryUserInfoDTO> getUserInfoByUid(List<Integer> uidList) {
        if (ListUtil.isEmpty(uidList)) {
            return Collections.emptyMap();
        }
        // 也可以改为循环调用queryViaCache
        Map<Integer, UserInfo> userInfoMap = userRedisHelper.queryMapViaCache(
                uidList, RedisKey.USER_INFO,
                userInfoRepository::findUserInfoByUid,
                UserInfo.class
        );
        if (userInfoMap == null || userInfoMap.isEmpty()) {
            throw new NotFountException("用户不存在");
        }

        Map<Integer, UserStats> userStatsMap = getUserStats(uidList);
        Map<Integer, QueryUserInfoDTO> resultMap = new HashMap<>();
        userInfoMap.forEach((uid, info) -> {
            QueryUserInfoDTO dto = entityConverter.copyFieldValue(info, QueryUserInfoDTO.class);
            dto.setLevel(UserInfoUtil.getLevel(dto.getExp()));
            UserStats stats = userStatsMap.get(uid);
            if (stats != null) {
                dto.setFollowerCount(stats.getFollowerCount());
                dto.setFansCount(stats.getFansCount());
                dto.setDynamicCount(stats.getDynamicCount());
            }
            resultMap.put(uid, dto);
        });

        return resultMap;
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
     * 查询用户关系（需要交叉检查互粉情况）
     *
     * @param fromUid
     * @param toUid
     * @return
     */
    @Override
    public QueryUserRelaDTO queryUserRela(Integer fromUid, Integer toUid) {
        UserRela positiveRela = userRelaRepository.queryUserRela(fromUid, toUid);
        if (positiveRela == null) {
            // 没有关注
            return new QueryUserRelaDTO(
                    fromUid, toUid,
                    false, false, false
            );
        }
        UserRela oppositeRela = userRelaRepository.queryUserRela(toUid, fromUid);
        if (oppositeRela == null) {
            // 单向关注
            return new QueryUserRelaDTO(
                    fromUid, toUid,
                    true, positiveRela.getSpecial().equals(1), false
            );
        }
        // 互相关注
        return new QueryUserRelaDTO(
                fromUid, toUid,
                true, positiveRela.getSpecial().equals(1), true
        );
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
            QueryUserInfoDTO queryUserInfoDTO = entityConverter.copyFieldValue(userInfo, QueryUserInfoDTO.class);
            queryUserInfoDTOList.add(queryUserInfoDTO);
        }

        return queryUserInfoDTOList;
    }


    /**
     * 批量查询用户的关注粉丝动态数
     * @param uidList
     * @return
     */
    private Map<Integer, UserStats> getUserStats(List<Integer> uidList) {
        SimpleResponse<QueryStatsDTO<UserStats>> response = statsFeignAPI.getUserStats(uidList);
        if (!response.isSuccess()) {
            throw new ServiceOperationException("查询用户统计数据失败");
        }
        return response.getData().getStatsMap();
    }

    /**
     * 查询关注用户或粉丝用户信息，并装填DTO
     * @param userRelaPage
     * @param originUid
     * @param follow
     * @return
     */
    private PageDTO<QueryFollowUserInfoDTO> handleUserRela(Page<UserRela> userRelaPage, Integer originUid, boolean follow) {
        Map<String, UserRela> userRelaMap = userRelaPage.getRecords()
                .stream()
                .collect(Collectors.toMap(
                        u -> getUserRelaMapKey(u.getFromUid(), u.getToUid()),
                        Function.identity()
                ));

        List<Integer> uidList = userRelaPage.getRecords()
                .stream()
                // 查询关注用户用toUid；查询粉丝用户用fromUid
                .map(follow? UserRela::getToUid : UserRela::getFromUid)
                .collect(Collectors.toList());

        // 查询用户信息
        Map<Integer, UserInfo> userInfoMap = userInfoRepository.findUserInfoByUid(uidList);

        // 查询关注粉丝数据
        Map<Integer, UserStats> userStatsMap = getUserStats(uidList);

        // 装填dto
        List<QueryFollowUserInfoDTO> dtoList = userInfoMap.values().stream()
                .map(userinfo -> {
                    QueryFollowUserInfoDTO dto =
                            entityConverter.copyFieldValue(
                                    userinfo, QueryFollowUserInfoDTO.class);
                    UserStats stats = userStatsMap.get(userinfo.getUid());
                    dto.setLevel(UserInfoUtil.getLevel(userinfo.getExp()));
                    if (stats != null) {
                        dto.setFollowerCount(stats.getFollowerCount());
                        dto.setFansCount(stats.getFansCount());
                        dto.setDynamicCount(stats.getDynamicCount());
                    }

                    // 查询特别关注状态
                    Integer fromUid = follow? originUid: userinfo.getUid();
                    Integer toUid = follow? userinfo.getUid(): originUid;
                    UserRela userRela = userRelaMap.get(getUserRelaMapKey(fromUid, toUid));
                    // todo: 对于查询粉丝用户 不需要special字段，即用户不需要知道粉丝有没有特别关注自己
                    if (userRela != null) {
                        Integer special = userRela.getSpecial();
                        dto.setSpecial(special == 1);
                    }

                    // 查询是否互粉
                    Boolean b;
                    if (follow) {
                        // 查询关注者有没有关注自己
                        b = userRelaRepository.existUserRela(userinfo.getUid(), originUid);
                    } else {
                        // 查询自己有没有关注该粉丝
                        b = userRelaRepository.existUserRela(originUid, userinfo.getUid());
                    }
                    dto.setIsMutual(b);

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

    private String getUserRelaMapKey(Integer fromUid, Integer toUid) {
        return String.join("->", fromUid.toString(), toUid.toString());
    }
}
