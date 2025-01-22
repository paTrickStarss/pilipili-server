/*
 * Copyright (c) 2025. Bubble
 */

package com.bubble.pilipili.video.pojo.req;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 新增视频信息请求数据
 * @author Bubble
 * @date 2025/01/20 18:25
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateVideoInfoReq {

    /**
     * 投稿用户ID
     */
    private Integer uid;
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
//    /**
//     * 投稿时间
//     */
//    private Date uploadDate;
//    /**
//     * 最后一次更新时间
//     */
//    private Date updateDate;
    /**
     * 发布时间（过审时间/定时发布时间）
     */
    private Date publishDate;
}
