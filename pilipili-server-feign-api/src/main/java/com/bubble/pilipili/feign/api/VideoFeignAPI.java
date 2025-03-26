/*
 * Copyright (c) 2025. Bubble
 */

package com.bubble.pilipili.feign.api;

import com.bubble.pilipili.common.http.SimpleResponse;
import com.bubble.pilipili.feign.pojo.req.UpdateVideoInfoReq;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

/**
 * @author Bubble
 * @date 2025.03.26 17:24
 */
@FeignClient("video-service")
public interface VideoFeignAPI {


    /**
     * 更新视频信息
     * @param req
     * @return
     */
    @Operation(summary = "更新视频信息")
    @PutMapping("/api/video/update")
    SimpleResponse<String> update(@Valid @RequestBody UpdateVideoInfoReq req);
}
