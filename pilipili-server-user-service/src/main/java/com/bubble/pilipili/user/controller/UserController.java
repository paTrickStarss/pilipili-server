/*
 * Copyright (c) 2024. Bubble
 */

package com.bubble.pilipili.user.controller;

import com.bubble.pilipili.common.constant.AuthConstant;
import com.bubble.pilipili.common.http.SimpleResponse;
import com.bubble.pilipili.user.pojo.dto.QueryUserInfoDTO;
import com.bubble.pilipili.user.pojo.dto.SaveUserInfoDTO;
import com.bubble.pilipili.user.pojo.req.SaveUserInfoReq;
import com.bubble.pilipili.user.service.UserInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

/**
 * 用户信息控制器
 * @author liweixin@hcrc1.wecom.work
 * @date 2024/10/14
 */
@Slf4j
@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserInfoService userInfoService;

    @GetMapping("/test")
    public SimpleResponse<String> test(HttpServletRequest request) {
        String jwtPayload = request.getHeader(AuthConstant.JWT_PAYLOAD_HEADER);
        log.debug("jwtPayload:{}", jwtPayload);
        return SimpleResponse.success("test");
    }

    /**
     * 注册用户
     * @param saveUserInfoReq
     * @return
     */
    @PostMapping("/register")
    public SimpleResponse<SaveUserInfoDTO> register(@Valid @RequestBody SaveUserInfoReq saveUserInfoReq) {
        SaveUserInfoDTO result = userInfoService.saveUserInfo(saveUserInfoReq);

        return SimpleResponse.success(result);
    }

    /**
     * 更新用户信息
     * @param request
     * @return
     */
    @PostMapping("/update")
    public SimpleResponse<SaveUserInfoDTO> update(@Valid @RequestBody SaveUserInfoReq request) {
        SaveUserInfoDTO result = userInfoService.updateUserInfo(request);
        return SimpleResponse.success(result);
    }

    @GetMapping("/getUser")
    public SimpleResponse<QueryUserInfoDTO> getUser(@Valid @RequestParam String uid) {
        QueryUserInfoDTO userInfoDTO = userInfoService.getUserInfoByUid(uid);
        return SimpleResponse.success(userInfoDTO);
    }

    /**
     * 查询所有用户
     * @return
     */
    @GetMapping("/listUser")
    public SimpleResponse<List<QueryUserInfoDTO>> listUser() {
        List<QueryUserInfoDTO> queryUserInfoDTOS = userInfoService.listUserInfo();
        return SimpleResponse.success(queryUserInfoDTOS);
    }

//    @PostMapping("/invalidate")
//    public SimpleResponse<SaveUserInfoDTO> invalidate(@Valid @RequestBody SaveUserInfoRequest request) {
//
//        return SimpleResponse.success()
//    }
}
