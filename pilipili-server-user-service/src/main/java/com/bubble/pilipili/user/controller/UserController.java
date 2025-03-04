/*
 * Copyright (c) 2024. Bubble
 */

package com.bubble.pilipili.user.controller;

import com.bubble.pilipili.common.constant.AuthConstant;
import com.bubble.pilipili.common.http.Controller;
import com.bubble.pilipili.common.http.SimpleResponse;
import com.bubble.pilipili.common.util.RSACryptoUtil;
import com.bubble.pilipili.user.pojo.dto.QueryUserInfoDTO;
import com.bubble.pilipili.user.pojo.dto.SaveUserInfoDTO;
import com.bubble.pilipili.user.pojo.req.RegisterReq;
import com.bubble.pilipili.user.pojo.req.SaveUserInfoReq;
import com.bubble.pilipili.user.service.UserInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.security.SignatureException;
import java.util.List;

/**
 * 用户信息控制器
 * @author liweixin@hcrc1.wecom.work
 * @date 2024/10/14
 */
@Slf4j
@RestController
@RequestMapping("/api/user")
public class UserController implements Controller {

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
     * @param registerReq
     * @return
     */
    @PostMapping("/register")
    public SimpleResponse<SaveUserInfoDTO> register(@Valid @RequestBody RegisterReq registerReq) {
        String encryptedPwd = registerReq.getPassword();
        String signature = registerReq.getSignature();
        String password;
        try {
            if (RSACryptoUtil.verify(encryptedPwd, signature)) {
                password = RSACryptoUtil.decrypt(encryptedPwd);
            } else {
                return SimpleResponse.failed("签名验证不通过！");
            }
        } catch (SignatureException | IllegalBlockSizeException | BadPaddingException e) {
            log.error(e.getMessage());
            return SimpleResponse.failed("参数解密失败！");
        }

        SaveUserInfoReq saveUserInfoReq = new SaveUserInfoReq();
        saveUserInfoReq.setPassword(password);
        saveUserInfoReq.setNickname(registerReq.getNickname());
        saveUserInfoReq.setEmail(registerReq.getEmail());

        SaveUserInfoDTO result = userInfoService.saveUserInfo(saveUserInfoReq);

        return SimpleResponse.success(result);
    }

    /**
     * 更新用户信息
     * @param request
     * @return
     */
    @PutMapping("/update")
    public SimpleResponse<SaveUserInfoDTO> update(@Valid @RequestBody SaveUserInfoReq request) {
        SaveUserInfoDTO result = userInfoService.updateUserInfo(request);
        return SimpleResponse.success(result);
    }

    @GetMapping("/{uid}")
    public SimpleResponse<QueryUserInfoDTO> getUser(@Valid @PathVariable String uid) {
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
