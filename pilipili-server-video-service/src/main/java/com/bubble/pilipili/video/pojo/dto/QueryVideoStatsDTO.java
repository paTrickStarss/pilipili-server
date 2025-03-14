/*
 * Copyright (c) 2025. Bubble
 */

package com.bubble.pilipili.video.pojo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 查询视频数据返回数据
 * @author Bubble
 * @date 2025.03.07 21:37
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class QueryVideoStatsDTO implements Serializable {


    /**
     * 视频ID
     */
    private Integer vid;
    /**
     * 播放数
     */
    private Long viewCount = 0L;
    /**
     * 弹幕数
     */
    private Long danmakuCount = 0L;
    /**
     * 评论数
     */
    private Long commentCount = 0L;
    /**
     * 点赞数
     */
    private Long favorCount = 0L;
    /**
     * 投币数
     */
    private Long coinCount = 0L;
    /**
     * 收藏数
     */
    private Long collectCount = 0L;
    /**
     * 转发数
     */
    private Long repostCount = 0L;
    /**
     * 点踩数
     */
    private Long dewCount = 0L;
}
