/*
 * Copyright (c) 2025. Bubble
 */

package com.bubble.pilipili.video.pojo.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.Version;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 用户视频互动关系实体类
 * @author Bubble
 * @date 2025.03.07 21:34
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("user_video")
public class UserVideo {

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
     * 更新时间
     */
    private LocalDateTime updateTime;
    /**
     * 最后一次观看时间
     */
    private LocalDateTime lastWatchTime;

    @Version
    private Integer version;
}
