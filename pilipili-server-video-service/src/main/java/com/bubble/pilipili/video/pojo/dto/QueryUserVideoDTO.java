/*
 * Copyright (c) 2025. Bubble
 */

package com.bubble.pilipili.video.pojo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 查询用户视频互动关系返回数据
 * @author Bubble
 * @date 2025.03.29 20:59
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class QueryUserVideoDTO implements Serializable {

    /**
     * 视频ID
     */
    private Integer vid;
    /**
     * 用户ID
     */
    private Integer uid;
    /**
     * 点赞
     */
    private Integer favor;
    /**
     * 投币
     */
    private Integer coin;
    /**
     * 收藏
     */
    private Integer collect;
    /**
     * 转发
     */
    private Integer repost;
    /**
     * 点踩
     */
    private Integer dew;
    /**
     * 最后一次观看时间
     */
    private LocalDateTime lastWatchTime;
}
