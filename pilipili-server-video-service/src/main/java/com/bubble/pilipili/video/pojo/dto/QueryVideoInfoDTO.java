/*
 * Copyright (c) 2025. Bubble
 */

package com.bubble.pilipili.video.pojo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author Bubble
 * @date 2025/01/22 23:04
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class QueryVideoInfoDTO implements Serializable {

    /**
     * 视频ID
     */
    private Integer vid;
    /**
     * 投稿用户ID
     */
    private Integer uid;
    /**
     * 投稿用户名
     */
    private String nickname;
    /**
     * 视频标题
     */
    private String title;
    /**
     * 视频简介
     */
    private String description;
    /**
     * 视频封面URL
     */
    private String coverUrl;
    /**
     * 视频内容URL
     */
    private String contentUrl;
    /**
     * 视频时长（秒）
     */
    private Integer duration;
    /**
     * 类型：0自制 1转载
     */
    private Integer sourceType;
    /**
     * 转载声明：0可自由转载 1未经许可不可转载
     */
    private Integer reprintPermit;
    /**
     * 视频标签 多标签用半角逗号隔开
     */
    private String tag;
    private List<String> tagList;
    /**
     * 视频分区主ID
     */
    private Integer primaryCategoryId;
    /**
     * 视频分区子ID
     */
    private Integer secondaryCategoryId;
    /**
     * 视频状态：0审核中 1审核通过 2审核不通过 3下架
     */
    private Integer status;
    /**
     * 投稿时间
     */
    private LocalDateTime uploadTime;
    /**
     * 最后一次更新时间
     */
    private LocalDateTime updateTime;
    /**
     * 发布时间（过审时间/定时发布时间）
     */
    private LocalDateTime publishTime;


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
