/*
 * Copyright (c) 2025. Bubble
 */

package com.bubble.pilipili.mq.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @author Bubble
 * @date 2025.03.15 14:49
 */
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class VideoStatsMessage extends StatsMessage {

    /**
     * 视频ID
     */
    private Integer vid;
    /**
     * 播放数
     */
    private Long viewCount;
    /**
     * 弹幕数
     */
    private Long danmakuCount;
    /**
     * 评论数
     */
    private Long commentCount;
    /**
     * 点赞数
     */
    private Long favorCount;
    /**
     * 投币数
     */
    private Long coinCount;
    /**
     * 收藏数
     */
    private Long collectCount;
    /**
     * 转发数
     */
    private Long repostCount;
    /**
     * 点踩数
     */
    private Long dewCount;
}
