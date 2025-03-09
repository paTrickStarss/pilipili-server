/*
 * Copyright (c) 2025. Bubble
 */

package com.bubble.pilipili.video.pojo.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.Version;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 视频统计数据实体类
 * @author Bubble
 * @date 2025.03.07 21:37
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("video_stats")
public class VideoStats {

    /**
     * 视频ID
     */
    @TableId
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
    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    @Version
    private Integer version;
}
