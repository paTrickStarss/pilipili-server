/*
 * Copyright (c) 2025. Bubble
 */

package com.bubble.pilipili.mq.controller;

import com.bubble.pilipili.common.http.Controller;
import com.bubble.pilipili.common.http.SimpleResponse;
import com.bubble.pilipili.common.pojo.converter.BaseConverter;
import com.bubble.pilipili.feign.api.StatsMQFeignAPI;
import com.bubble.pilipili.feign.pojo.req.SendCommentStatsReq;
import com.bubble.pilipili.feign.pojo.req.SendDanmakuStatsReq;
import com.bubble.pilipili.feign.pojo.req.SendDynamicStatsReq;
import com.bubble.pilipili.feign.pojo.req.SendVideoStatsReq;
import com.bubble.pilipili.mq.entity.CommentStatsMessage;
import com.bubble.pilipili.mq.entity.DanmakuStatsMessage;
import com.bubble.pilipili.mq.entity.DynamicStatsMessage;
import com.bubble.pilipili.mq.entity.VideoStatsMessage;
import com.bubble.pilipili.mq.producer.StatsMessageProducer;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * 统计数据消息发送管理控制器
 * @author Bubble
 * @date 2025.03.14 18:58
 */
@RestController
@RequestMapping("/mq/stats")
@Tag(name = "StatsMQController", description = "统计数据消息发送管理接口")
public class StatsMQController implements StatsMQFeignAPI, Controller {

    @Autowired
    private StatsMessageProducer statsMessageProducer;

    /**
     * 发送评论统计数据消息
     * @param req
     * @return
     */
    @Operation(summary = "发送评论统计数据消息")
    @PostMapping("/comment")
    public SimpleResponse<String> sendCommentStats(@Valid @RequestBody SendCommentStatsReq req) {
        CommentStatsMessage message = BaseConverter.getInstance().copyFieldValue(req, CommentStatsMessage.class);
        statsMessageProducer.sendCommentStats(message);
        return SimpleResponse.success("");
    }

    /**
     * 发送动态统计数据消息
     * @param req
     * @return
     */
    @Operation(summary = "发送动态统计数据消息")
    @PostMapping("/dynamic")
    public SimpleResponse<String> sendDynamicStats(@Valid @RequestBody SendDynamicStatsReq req) {
        DynamicStatsMessage message = BaseConverter.getInstance().copyFieldValue(req, DynamicStatsMessage.class);
        statsMessageProducer.sendDynamicStats(message);
        return SimpleResponse.success("");
    }

    /**
     * 发送弹幕统计数据消息
     * @param req
     * @return
     */
    @Operation(summary = "发送弹幕统计数据消息")
    @PostMapping("/danmaku")
    public SimpleResponse<String> sendDanmakuStats(@Valid @RequestBody SendDanmakuStatsReq req) {
        DanmakuStatsMessage message = BaseConverter.getInstance().copyFieldValue(req, DanmakuStatsMessage.class);
        statsMessageProducer.sendDanmakuStats(message);
        return SimpleResponse.success("");
    }

    /**
     * 发送视频统计数据消息
     * @param req
     * @return
     */
    @Operation(summary = "发送视频统计数据消息")
    @PostMapping("/video")
    public SimpleResponse<String> sendVideoStats(@Valid @RequestBody SendVideoStatsReq req) {
        VideoStatsMessage message = BaseConverter.getInstance().copyFieldValue(req, VideoStatsMessage.class);
        statsMessageProducer.sendVideoStats(message);
        return SimpleResponse.success("");
    }

}
