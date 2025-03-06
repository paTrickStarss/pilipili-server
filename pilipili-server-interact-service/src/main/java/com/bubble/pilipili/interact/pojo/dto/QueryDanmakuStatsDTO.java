/*
 * Copyright (c) 2025. Bubble
 */

package com.bubble.pilipili.interact.pojo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author Bubble
 * @date 2025.03.06 14:37
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class QueryDanmakuStatsDTO implements Serializable {

    /**
     * 弹幕ID
     */
    private Integer danmakuId;
    /**
     * 点赞数
     */
    private Integer favorCount;
    /**
     * 点踩数
     */
    private Integer dewCount;
}
