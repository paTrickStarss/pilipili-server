package com.bubble.pilipili.user.controller;

import com.bubble.pilipili.common.constant.AuthConstant;
import com.bubble.pilipili.common.http.SimpleResponse;
import com.bubble.pilipili.user.pojo.dto.SaveUserInfoDTO;
import com.bubble.pilipili.user.pojo.req.SaveUserInfoRequest;
import com.bubble.pilipili.user.service.UserInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

/**
 * 用户信息控制器
 * @author liweixin@hcrc1.wecom.work
 * @date 2024/10/14
 */
@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    private UserInfoService userInfoService;

    @GetMapping("/test")
    public SimpleResponse<String> test(HttpServletRequest request) {
        String jwtPayload = request.getHeader(AuthConstant.JWT_PAYLOAD_HEADER);
        log.debug("jwtPayload:{}", jwtPayload);
        return SimpleResponse.success("test");
    }

    /**
     * 注册用户
     * @param request
     * @return
     */
    @PostMapping("/register")
    public SimpleResponse<SaveUserInfoDTO> register(@Valid @RequestBody SaveUserInfoRequest request) {
        SaveUserInfoDTO result = userInfoService.saveUserInfo(request);

        return SimpleResponse.success(result);
    }
}
