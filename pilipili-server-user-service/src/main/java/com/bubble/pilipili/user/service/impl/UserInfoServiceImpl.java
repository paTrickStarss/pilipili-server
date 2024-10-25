package com.bubble.pilipili.user.service.impl;

import com.bubble.pilipili.user.pojo.converter.UserInfoConverter;
import com.bubble.pilipili.user.pojo.dto.QueryUserInfoDTO;
import com.bubble.pilipili.user.pojo.dto.SaveUserInfoDTO;
import com.bubble.pilipili.user.pojo.entity.UserInfo;
import com.bubble.pilipili.user.pojo.req.SaveUserInfoRequest;
import com.bubble.pilipili.user.pojo.req.UpdateUserInfoRequest;
import com.bubble.pilipili.user.repository.UserInfoRepository;
import com.bubble.pilipili.user.service.UserInfoService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;

/**
 * 用户信息业务
 * @author liweixin@hcrc1.wecom.work
 * @date 2024/10/23
 */
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
    public SaveUserInfoDTO saveUserInfo(SaveUserInfoRequest saveUserInfoRequest) {
        UserInfo userInfo = UserInfoConverter.fromSaveRequest(saveUserInfoRequest);

        Integer i = userInfoRepository.saveUserInfo(userInfo);
        SaveUserInfoDTO dto = new SaveUserInfoDTO();
        if (i == 1) {
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
     * @param updateUserInfoRequest
     * @return
     */
    @Override
    public Integer updateUserInfo(UpdateUserInfoRequest updateUserInfoRequest) {
        return 0;
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
        return Collections.emptyList();
    }
}
