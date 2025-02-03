/*
 * Copyright (c) 2024. Bubble
 */

package com.bubble.pilipili.user.service.impl;

import com.bubble.pilipili.user.pojo.converter.UserInfoConverter;
import com.bubble.pilipili.user.pojo.dto.QueryUserInfoDTO;
import com.bubble.pilipili.user.pojo.dto.SaveUserInfoDTO;
import com.bubble.pilipili.user.pojo.entity.UserInfo;
import com.bubble.pilipili.user.pojo.req.SaveUserInfoReq;
import com.bubble.pilipili.user.repository.UserInfoRepository;
import com.bubble.pilipili.user.service.UserInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * 用户信息业务类
 * @author liweixin@hcrc1.wecom.work
 * @date 2024/10/23
 */
@Slf4j
@Service("UserInfoService")
public class UserInfoServiceImpl implements UserInfoService {

    @Autowired
    private UserInfoRepository userInfoRepository;

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
    public QueryUserInfoDTO getUserInfoByUid(String uid) {
        UserInfo userInfo = userInfoRepository.findUserInfoByUid(Integer.parseInt(uid));
        return UserInfoConverter.getInstance().copyFieldValue(userInfo, QueryUserInfoDTO.class);
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

}
