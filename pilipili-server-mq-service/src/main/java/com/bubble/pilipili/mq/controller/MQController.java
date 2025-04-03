/*
 * Copyright (c) 2025. Bubble
 */

package com.bubble.pilipili.mq.controller;

import com.bubble.pilipili.common.http.Controller;
import com.bubble.pilipili.common.http.SimpleResponse;
import com.bubble.pilipili.common.pojo.converter.BaseConverter;
import com.bubble.pilipili.feign.api.MQFeignAPI;
import com.bubble.pilipili.feign.pojo.req.*;
import com.bubble.pilipili.mq.entity.*;
import com.bubble.pilipili.mq.producer.MessageProducer;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * MQ消息发送管理接口控制器
 * @author Bubble
 * @date 2025.03.14 18:58
 */
@RestController
@Tag(name = "MQController", description = "MQ消息发送管理接口")
public class MQController implements MQFeignAPI, Controller {

    @Autowired
    private MessageProducer messageProducer;

    /**
     * 发送评论统计数据消息
     * @param req
     * @return
     */
    @Operation(summary = "发送评论统计数据消息")
    @PostMapping("/mq/stats/comment")
    public SimpleResponse<String> sendCommentStats(@Valid @RequestBody SendCommentStatsReq req) {
        CommentStatsMessage message = BaseConverter.getInstance().copyFieldValue(req, CommentStatsMessage.class);
        messageProducer.sendCommentStats(message);
        return SimpleResponse.success("");
    }

    /**
     * 发送动态统计数据消息
     * @param req
     * @return
     */
    @Operation(summary = "发送动态统计数据消息")
    @PostMapping("/mq/stats/dynamic")
    public SimpleResponse<String> sendDynamicStats(@Valid @RequestBody SendDynamicStatsReq req) {
        DynamicStatsMessage message = BaseConverter.getInstance().copyFieldValue(req, DynamicStatsMessage.class);
        messageProducer.sendDynamicStats(message);
        return SimpleResponse.success("");
    }

    /**
     * 发送弹幕统计数据消息
     * @param req
     * @return
     */
    @Operation(summary = "发送弹幕统计数据消息")
    @PostMapping("/mq/stats/danmaku")
    public SimpleResponse<String> sendDanmakuStats(@Valid @RequestBody SendDanmakuStatsReq req) {
        DanmakuStatsMessage message = BaseConverter.getInstance().copyFieldValue(req, DanmakuStatsMessage.class);
        messageProducer.sendDanmakuStats(message);
        return SimpleResponse.success("");
    }

    /**
     * 发送视频统计数据消息
     * @param req
     * @return
     */
    @Operation(summary = "发送视频统计数据消息")
    @PostMapping("/mq/stats/video")
    public SimpleResponse<String> sendVideoStats(@Valid @RequestBody SendVideoStatsReq req) {
        VideoStatsMessage message = BaseConverter.getInstance().copyFieldValue(req, VideoStatsMessage.class);
        messageProducer.sendVideoStats(message);
        return SimpleResponse.success("");
    }

//    /**
//     * 发送视频信息更新消息
//     * @param req
//     * @return
//     */
//    @Operation(summary = "发送视频信息更新消息")
//    @PostMapping("/mq/update/video")
//    public SimpleResponse<String> sendVideoInfo(@Valid @RequestBody SendVideoInfoReq req) {
//        VideoInfoMessage message = BaseConverter.getInstance().copyFieldValue(req, VideoInfoMessage.class);
//        messageProducer.sendVideoInfo(message);
//        return SimpleResponse.success("");
//    }

    /**
     * 发送视频信息更新消息
     * @param req
     * @return
     */
    @Operation(summary = "发送视频信息更新消息")
    @PostMapping("/mq/update/video")
    public SimpleResponse<String> sendVideoInfo(@Valid @RequestBody SendVideoInfoReq req) {
        VideoInfoMessage message = BaseConverter.getInstance().copyFieldValue(req, VideoInfoMessage.class);
        // 第一次立即消费，没有延迟
        messageProducer.sendVideoInfo(message, 0, 0);
        return SimpleResponse.success("");
    }

}
