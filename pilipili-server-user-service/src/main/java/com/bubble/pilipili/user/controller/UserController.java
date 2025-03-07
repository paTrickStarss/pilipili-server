/*
 * Copyright (c) 2024-2025. Bubble
 */

package com.bubble.pilipili.user.controller;

import com.bubble.pilipili.common.constant.AuthConstant;
import com.bubble.pilipili.common.http.Controller;
import com.bubble.pilipili.common.http.PageResponse;
import com.bubble.pilipili.common.http.SimpleResponse;
import com.bubble.pilipili.common.pojo.PageDTO;
import com.bubble.pilipili.common.util.RSACryptoUtil;
import com.bubble.pilipili.user.pojo.dto.QueryFollowUserInfoDTO;
import com.bubble.pilipili.user.pojo.dto.QueryUserInfoDTO;
import com.bubble.pilipili.user.pojo.dto.SaveUserInfoDTO;
import com.bubble.pilipili.user.pojo.req.PageQueryUserInfoReq;
import com.bubble.pilipili.user.pojo.req.RegisterReq;
import com.bubble.pilipili.user.pojo.req.SaveUserInfoReq;
import com.bubble.pilipili.user.service.UserInfoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.security.SignatureException;
import java.util.List;
import java.util.Objects;

/**
 * 用户信息控制器
 * @author liweixin@hcrc1.wecom.work
 * @date 2024/10/14
 */
@Slf4j
@RestController
@RequestMapping("/api/user")
@Tag(name = "UserController", description = "用户管理相关接口")
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
    @Operation(summary = "注册用户")
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
        } catch (SignatureException | IllegalBlockSizeException | BadPaddingException | IllegalArgumentException e) {
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
    @Operation(summary = "更新用户信息")
    @PutMapping("/update")
    public SimpleResponse<SaveUserInfoDTO> update(@Valid @RequestBody SaveUserInfoReq request) {
        SaveUserInfoDTO result = userInfoService.updateUserInfo(request);
        return SimpleResponse.success(result);
    }

    /**
     * 关注用户
     * @param fromUid
     * @param toUid
     * @param special
     * @return
     */
    @Operation(summary = "关注用户")
    @PatchMapping("/follow")
    public SimpleResponse<String> follow(
            @NotBlank @RequestParam Integer fromUid,
            @NotBlank @RequestParam Integer toUid,
            @RequestParam(required = false, defaultValue = "0") Integer special
    ) {
        if (Objects.equals(fromUid, toUid)) {
            return SimpleResponse.failed("不能关注自己哦");
        }
        Boolean b = userInfoService.followUser(fromUid, toUid, special == 1);
        return SimpleResponse.result(b);
    }

    /**
     * 取消关注用户
     * @param fromUid
     * @param toUid
     * @return
     */
    @Operation(summary = "取消关注用户")
    @PatchMapping("/unfollow")
    public SimpleResponse<String> unfollow(
            @NotBlank @RequestParam Integer fromUid,
            @NotBlank @RequestParam Integer toUid
    ) {
        Boolean b = userInfoService.unfollowUser(fromUid, toUid);
        return SimpleResponse.result(b);
    }

    /**
     * 查询用户信息
     * @param uid
     * @return
     */
    @Operation(summary = "查询用户信息")
    @GetMapping("/{uid}")
    public SimpleResponse<QueryUserInfoDTO> getUser(@Valid @PathVariable Integer uid) {
        QueryUserInfoDTO userInfoDTO = userInfoService.getUserInfoByUid(uid);
        return SimpleResponse.success(userInfoDTO);
    }

    /**
     * 查询关注用户
     * @param req
     * @return
     */
    @Operation(summary = "查询关注用户")
    @GetMapping("/pageQueryFollowers")
    public PageResponse<QueryFollowUserInfoDTO> pageQueryFollowers(
            @Valid @ModelAttribute PageQueryUserInfoReq req
    ) {
        PageDTO<QueryFollowUserInfoDTO> dto = userInfoService.pageQueryFollowers(req);
        return PageResponse.success(dto);
    }

    /**
     * 查询粉丝用户
     * @param req
     * @return
     */
    @Operation(summary = "查询粉丝用户")
    @GetMapping("/pageQueryFans")
    public PageResponse<QueryFollowUserInfoDTO> pageQueryFans(
            @Valid @ModelAttribute PageQueryUserInfoReq req
    ) {
        PageDTO<QueryFollowUserInfoDTO> dto = userInfoService.pageQueryFans(req);
        return PageResponse.success(dto);
    }

    /**
     * 查询所有用户（管理员）
     * @return
     */
    @Operation(summary = "查询所有用户")
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
