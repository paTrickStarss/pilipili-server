/*
 * Copyright (c) 2025. Bubble
 */

package com.bubble.pilipili.feign.pojo.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.bubble.pilipili.common.pojo.StatsEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 视频统计数据实体类
 * @author Bubble
 * @date 2025.03.07 21:37
 */
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("video_stats")
public class VideoStats extends StatsEntity {

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
