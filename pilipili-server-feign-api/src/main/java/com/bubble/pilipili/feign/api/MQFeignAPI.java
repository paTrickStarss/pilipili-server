/*
 * Copyright (c) 2025. Bubble
 */

package com.bubble.pilipili.feign.api;

import com.bubble.pilipili.common.http.SimpleResponse;
import com.bubble.pilipili.feign.pojo.req.*;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

/**
 * @author Bubble
 * @date 2025.03.15 15:01
 */
@FeignClient("mq-service")
public interface MQFeignAPI {

    /**
     * 发送评论统计数据消息
     * @param req
     * @return
     */
    @Operation(summary = "发送评论统计数据消息")
    @PostMapping("/mq/stats/comment")
    SimpleResponse<String> sendCommentStats(@Valid @RequestBody SendCommentStatsReq req);

    /**
     * 发送动态统计数据消息
     * @param req
     * @return
     */
    @Operation(summary = "发送动态统计数据消息")
    @PostMapping("/mq/stats/dynamic")
    SimpleResponse<String> sendDynamicStats(@Valid @RequestBody SendDynamicStatsReq req);

    /**
     * 发送弹幕统计数据消息
     * @param req
     * @return
     */
    @Operation(summary = "发送弹幕统计数据消息")
    @PostMapping("/mq/stats/danmaku")
    SimpleResponse<String> sendDanmakuStats(@Valid @RequestBody SendDanmakuStatsReq req);

    /**
     * 发送视频统计数据消息
     * @param req
     * @return
     */
    @Operation(summary = "发送视频统计数据消息")
    @PostMapping("/mq/stats/video")
    SimpleResponse<String> sendVideoStats(@Valid @RequestBody SendVideoStatsReq req);

    /**
     * 发送视频信息更新消息
     * @param req
     * @return
     */
    @Operation(summary = "发送视频信息更新消息")
    @PostMapping("/mq/update/video")
    SimpleResponse<String> sendVideoInfo(@Valid @RequestBody SendVideoInfoReq req);
}
