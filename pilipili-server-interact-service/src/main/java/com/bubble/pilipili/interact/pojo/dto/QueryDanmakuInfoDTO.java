/*
 * Copyright (c) 2025. Bubble
 */

package com.bubble.pilipili.interact.pojo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author Bubble
 * @date 2025.03.06 14:04
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class QueryDanmakuInfoDTO implements Serializable {

    /**
     * 弹幕ID
     */
    private Integer danmakuId;
    /**
     * 所在视频ID
     */
    private Integer vid;
    /**
     * 发送用户ID
     */
    private Integer uid;
    /**
     * 弹幕出现时刻（秒）
     */
    private Integer timing;
    /**
     * 弹幕内容
     */
    private String content;
    /**
     * 字体大小 0小 1中 2大
     */
    private Integer fontSize;
    /**
     * 弹幕类型 0滚动 1顶部 2底部
     */
    private Integer danmakuType;
    /**
     * 弹幕颜色
     */
    private String color;
    /**
     * 发送时间
     */
    private LocalDateTime createTime;

    /**
     * 点赞数
     */
    private Integer favorCount = 0;
    /**
     * 点踩数
     */
    private Integer dewCount = 0;
}
