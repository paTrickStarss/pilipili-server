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
    private Integer vid = 0;
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

    public Integer getVid() {
        return vid == null ? 0 : vid;
    }

    public Long getViewCount() {
        return viewCount == null ? 0 : viewCount;
    }

    public Long getDanmakuCount() {
        return danmakuCount == null ? 0 : danmakuCount;
    }

    public Long getCommentCount() {
        return commentCount == null ? 0 : commentCount;
    }

    public Long getFavorCount() {
        return favorCount == null ? 0 : favorCount;
    }

    public Long getCoinCount() {
        return coinCount == null ? 0 : coinCount;
    }

    public Long getCollectCount() {
        return collectCount == null ? 0 : collectCount;
    }

    public Long getRepostCount() {
        return repostCount == null ? 0 : repostCount;
    }

    public Long getDewCount() {
        return dewCount == null ? 0 : dewCount;
    }
}
