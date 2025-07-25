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
 * @date 2025.03.05 21:28
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class QueryCommentStatsDTO implements Serializable {

    /**
     * 评论ID
     */
    private Integer cid;
    /**
     * 点赞数
     */
    private Integer favorCount;
    /**
     * 点踩数
     */
    private Integer dewCount;
}
