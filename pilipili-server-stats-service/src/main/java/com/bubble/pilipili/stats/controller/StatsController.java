/*
 * Copyright (c) 2025. Bubble
 */

package com.bubble.pilipili.stats.controller;

import com.bubble.pilipili.common.http.Controller;
import com.bubble.pilipili.common.http.SimpleResponse;
import com.bubble.pilipili.feign.api.StatsFeignAPI;
import com.bubble.pilipili.feign.pojo.dto.QueryStatsDTO;
import com.bubble.pilipili.feign.pojo.entity.*;
import com.bubble.pilipili.stats.service.*;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * @author Bubble
 * @date 2025.03.15 20:23
 */
@RestController
public class StatsController implements StatsFeignAPI, Controller {

    @Autowired
    private VideoStatsService videoStatsService;
    @Autowired
    private CommentStatsService commentStatsService;
    @Autowired
    private DynamicStatsService dynamicStatsService;
    @Autowired
    private DanmakuStatsService danmakuStatsService;
    @Autowired
    private UserStatsService userStatsService;


    /**
     * 保存视频统计数据
     *
     * @param stats
     * @return
     */
    @Override
    @Operation(summary = "保存视频统计数据")
    @PostMapping("/api/stats/video")
    public SimpleResponse<String> saveVideoStats(@Valid @RequestBody VideoStats stats) {
        Boolean b = videoStatsService.saveStats(stats);
        return SimpleResponse.result(b);
    }

    /**
     * 查询视频统计数据
     *
     * @param idList
     * @return
     */
    @Override
    @Operation(summary = "查询视频统计数据")
    @GetMapping("/api/stats/video")
    public SimpleResponse<QueryStatsDTO<VideoStats>> getVideoStats(@Valid @RequestParam List<Integer> idList) {
        QueryStatsDTO<VideoStats> stats = videoStatsService.getStats(idList);
        return SimpleResponse.success(stats);
    }

    /**
     * 保存动态统计数据
     * @param stats
     * @return
     */
    @Operation(summary = "保存动态统计数据")
    @PostMapping("/api/stats/dynamic")
    public SimpleResponse<String> saveDynamicStats(@Valid @RequestBody DynamicStats stats) {
        Boolean b = dynamicStatsService.saveStats(stats);
        return SimpleResponse.result(b);
    }

    /**
     * 查询动态统计数据
     * @param idList
     * @return
     */
    @Operation(summary = "查询动态统计数据")
    @GetMapping("/api/stats/dynamic")
    public SimpleResponse<QueryStatsDTO<DynamicStats>> getDynamicStats(@Valid @RequestParam List<Integer> idList) {
        QueryStatsDTO<DynamicStats> stats = dynamicStatsService.getStats(idList);
        return SimpleResponse.success(stats);
    }

    /**
     * 保存弹幕统计数据
     * @param stats
     * @return
     */
    @Operation(summary = "保存弹幕统计数据")
    @PostMapping("/api/stats/danmaku")
    public SimpleResponse<String> saveDanmakuStats(@Valid @RequestBody DanmakuStats stats) {
        Boolean b = danmakuStatsService.saveStats(stats);
        return SimpleResponse.result(b);
    }

    /**
     * 查询弹幕统计数据
     * @param idList
     * @return
     */
    @Operation(summary = "查询弹幕统计数据")
    @GetMapping("/api/stats/danmaku")
    public SimpleResponse<QueryStatsDTO<DanmakuStats>> getDanmakuStats(@Valid @RequestParam List<Integer> idList) {
        QueryStatsDTO<DanmakuStats> stats = danmakuStatsService.getStats(idList);
        return SimpleResponse.success(stats);
    }

    /**
     * 保存评论统计数据
     * @param stats
     * @return
     */
    @Operation(summary = "保存评论统计数据")
    @PostMapping("/api/stats/comment")
    public SimpleResponse<String> saveCommentStats(@Valid @RequestBody CommentStats stats) {
        Boolean b = commentStatsService.saveStats(stats);
        return SimpleResponse.result(b);
    }

    /**
     * 查询评论统计数据
     * @param idList
     * @return
     */
    @Operation(summary = "查询评论统计数据")
    @GetMapping("/api/stats/comment")
    public SimpleResponse<QueryStatsDTO<CommentStats>> getCommentStats(@Valid @RequestParam List<Integer> idList) {
        QueryStatsDTO<CommentStats> stats = commentStatsService.getStats(idList);
        return SimpleResponse.success(stats);
    }

    /**
     * 保存用户统计数据
     * @param stats
     * @return
     */
    @Operation(summary = "保存用户统计数据")
    @PostMapping("/api/stats/user")
    public SimpleResponse<String> saveUserStats(@Valid @RequestBody UserStats stats) {
        Boolean b = userStatsService.saveStats(stats);
        return SimpleResponse.result(b);
    }

    /**
     * 查询用户统计数据
     * @param idList
     * @return
     */
    @Operation(summary = "查询用户统计数据")
    @GetMapping("/api/stats/user")
    public SimpleResponse<QueryStatsDTO<UserStats>> getUserStats(@Valid @RequestParam List<Integer> idList) {
        QueryStatsDTO<UserStats> stats = userStatsService.getStats(idList);
        return SimpleResponse.success(stats);
    }
}
