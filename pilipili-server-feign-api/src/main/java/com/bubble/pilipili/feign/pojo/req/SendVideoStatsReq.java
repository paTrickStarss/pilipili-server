/*
 * Copyright (c) 2025. Bubble
 */

package com.bubble.pilipili.feign.pojo.req;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author Bubble
 * @date 2025.03.15 14:57
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SendVideoStatsReq implements Serializable {
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
