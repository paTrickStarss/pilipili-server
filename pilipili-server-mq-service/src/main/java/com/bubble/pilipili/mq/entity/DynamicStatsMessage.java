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
 * @date 2025.03.15 14:48
 */
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DynamicStatsMessage extends StatsMessage {
    private Integer did = 0;
    private Long favorCount = 0L;
    private Long commentCount = 0L;
    private Long repostCount = 0L;

    public Integer getDid() {
        return did == null ? 0 : did;
    }

    public Long getFavorCount() {
        return favorCount == null ? 0 : favorCount;
    }

    public Long getCommentCount() {
        return commentCount == null ? 0 : commentCount;
    }

    public Long getRepostCount() {
        return repostCount == null ? 0 : repostCount;
    }
}
