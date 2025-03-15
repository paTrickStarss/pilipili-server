/*
 * Copyright (c) 2025. Bubble
 */

package com.bubble.pilipili.feign.api;

import com.bubble.pilipili.common.http.SimpleResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;

/**
 * 动态管理服务远程调用客户端
 * @author Bubble
 * @date 2025.03.14 14:43
 */
@FeignClient("interact-service")
@Tag(name = "DynamicFeignAPI", description = "动态管理服务远程调用客户端")
public interface DynamicFeignAPI {

    /**
     * 评论动态
     * @param did
     * @param uid
     * @return
     */
    @Operation(summary = "评论动态")
    @PatchMapping("/api/interact/dynamic/comment")
    SimpleResponse<String> comment(
            @Valid @RequestParam Integer did,
            @Valid @RequestParam Integer uid
    );
}
