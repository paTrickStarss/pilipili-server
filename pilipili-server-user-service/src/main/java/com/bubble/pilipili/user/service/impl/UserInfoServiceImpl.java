package com.bubble.pilipili.user.service.impl;

import com.bubble.pilipili.user.pojo.converter.UserInfoConverter;
import com.bubble.pilipili.user.pojo.dto.QueryUserInfoDTO;
import com.bubble.pilipili.user.pojo.dto.SaveUserInfoDTO;
import com.bubble.pilipili.user.pojo.entity.UserInfo;
import com.bubble.pilipili.user.pojo.req.SaveUserInfoRequest;
import com.bubble.pilipili.user.repository.UserInfoRepository;
import com.bubble.pilipili.user.service.UserInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * 用户信息业务类
 * @author liweixin@hcrc1.wecom.work
 * @date 2024/10/23
 */
@Slf4j
@Service("UserInfoService")
public class UserInfoServiceImpl implements UserInfoService {

    @Resource
    private UserInfoRepository userInfoRepository;

    /**
     * 保存用户信息
     *
     * @param saveUserInfoRequest
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public SaveUserInfoDTO saveUserInfo(SaveUserInfoRequest saveUserInfoRequest) {
        long l1 = System.currentTimeMillis();
        UserInfo userInfo = UserInfoConverter.getInstance().copyFieldValue(saveUserInfoRequest, UserInfo.class);
        log.debug("copyFieldValue cost: {} ms", System.currentTimeMillis() - l1);

        Boolean saveSuccess = userInfoRepository.saveUserInfo(userInfo);
        SaveUserInfoDTO dto = new SaveUserInfoDTO();
        if (saveSuccess) {
            dto.setSuccess(Boolean.TRUE);
            dto.setUid(userInfo.getUid());
        } else {
            dto.setSuccess(Boolean.FALSE);
        }

        return dto;
    }

    /**
     * 更新用户信息
     *
     * @param saveUserInfoRequest
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public SaveUserInfoDTO updateUserInfo(SaveUserInfoRequest saveUserInfoRequest) {
        UserInfo userInfo = UserInfoConverter.getInstance().copyFieldValue(saveUserInfoRequest, UserInfo.class);
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
        return null;
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
     * 注销用户
     *
     * @param saveUserInfoRequest
     * @return
     */
    @Override
    public SaveUserInfoDTO invalidateUser(SaveUserInfoRequest saveUserInfoRequest) {
        return null;
    }

    /**
     * 封禁用户
     *
     * @param saveUserInfoRequest
     * @return
     */
    @Override
    public SaveUserInfoDTO blockUser(SaveUserInfoRequest saveUserInfoRequest) {
        return null;
    }
}
