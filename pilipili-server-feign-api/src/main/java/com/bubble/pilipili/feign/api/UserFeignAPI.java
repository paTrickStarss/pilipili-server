/*
 * Copyright (c) 2025. Bubble
 */

package com.bubble.pilipili.feign.api;

import com.bubble.pilipili.common.http.SimpleResponse;
import com.bubble.pilipili.feign.pojo.dto.QueryUserInfoDTO;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

/**
 * @author Bubble
 * @date 2025.05.17 21:52
 */
@FeignClient("user-service")
public interface UserFeignAPI {

    /**
     * 查询用户信息
     * @param uid
     * @return
     */
    @Operation(summary = "查询用户信息")
    @GetMapping("/api/user/{uid}")
    SimpleResponse<QueryUserInfoDTO> getUser(@Valid @PathVariable Integer uid);

    /**
     * 批量查询用户信息
     * @param uidList
     * @return
     */
    @Operation(summary = "批量查询用户信息")
    @GetMapping("/api/user/getUserInfo")
    SimpleResponse<Map<Integer, QueryUserInfoDTO>> getUserInfo(@Valid @RequestParam List<Integer> uidList);
}
