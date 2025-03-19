/*
 * Copyright (c) 2025. Bubble
 */

package com.bubble.pilipili.feign.api;

import com.bubble.pilipili.common.http.SimpleResponse;
import com.bubble.pilipili.feign.pojo.dto.QueryStatsDTO;
import com.bubble.pilipili.feign.pojo.entity.*;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
import java.util.List;

/**
 * @author Bubble
 * @date 2025.03.15 21:20
 */
@FeignClient("stats-service")
public interface StatsFeignAPI {

    /**
     * 保存视频统计数据
     * @param stats
     * @return
     */
    @Operation(summary = "保存视频统计数据")
    @PostMapping("/api/stats/video")
    SimpleResponse<String> saveVideoStats(@Valid @RequestBody VideoStats stats);

    /**
     * 查询视频统计数据
     * @param idList
     * @return
     */
    @Operation(summary = "查询视频统计数据")
    @GetMapping("/api/stats/video")
    SimpleResponse<QueryStatsDTO<VideoStats>> getVideoStats(@Valid @RequestParam List<Integer> idList);

    /**
     * 保存动态统计数据
     * @param stats
     * @return
     */
    @Operation(summary = "保存动态统计数据")
    @PostMapping("/api/stats/dynamic")
    SimpleResponse<String> saveDynamicStats(@Valid @RequestBody DynamicStats stats);

    /**
     * 查询动态统计数据
     * @param idList
     * @return
     */
    @Operation(summary = "查询动态统计数据")
    @GetMapping("/api/stats/dynamic")
    SimpleResponse<QueryStatsDTO<DynamicStats>> getDynamicStats(@Valid @RequestParam List<Integer> idList);

    /**
     * 保存弹幕统计数据
     * @param stats
     * @return
     */
    @Operation(summary = "保存弹幕统计数据")
    @PostMapping("/api/stats/danmaku")
    SimpleResponse<String> saveDanmakuStats(@Valid @RequestBody DanmakuStats stats);

    /**
     * 查询弹幕统计数据
     * @param idList
     * @return
     */
    @Operation(summary = "查询弹幕统计数据")
    @GetMapping("/api/stats/danmaku")
    SimpleResponse<QueryStatsDTO<DanmakuStats>> getDanmakuStats(@Valid @RequestParam List<Integer> idList);

    /**
     * 保存评论统计数据
     * @param stats
     * @return
     */
    @Operation(summary = "保存评论统计数据")
    @PostMapping("/api/stats/comment")
    SimpleResponse<String> saveCommentStats(@Valid @RequestBody CommentStats stats);

    /**
     * 查询评论统计数据
     * @param idList
     * @return
     */
    @Operation(summary = "查询评论统计数据")
    @GetMapping("/api/stats/comment")
    SimpleResponse<QueryStatsDTO<CommentStats>> getCommentStats(@Valid @RequestParam List<Integer> idList);

    /**
     * 保存用户统计数据
     * @param stats
     * @return
     */
    @Operation(summary = "保存用户统计数据")
    @PostMapping("/api/stats/user")
    SimpleResponse<String> saveUserStats(@Valid @RequestBody UserStats stats);

    /**
     * 查询用户统计数据
     * @param idList
     * @return
     */
    @Operation(summary = "查询用户统计数据")
    @GetMapping("/api/stats/user")
    SimpleResponse<QueryStatsDTO<UserStats>> getUserStats(@Valid @RequestParam List<Integer> idList);
}
