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
 * @date 2025.03.15 14:43
 */
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentStatsMessage extends StatsMessage {
    private Integer cid = 0;
    private Long favorCount = 0L;
    private Long dewCount = 0L;

    public Integer getCid() {
        return cid == null ? 0 : cid;
    }

    public Long getFavorCount() {
        return favorCount == null ? 0 : favorCount;
    }

    public Long getDewCount() {
        return dewCount == null ? 0 : dewCount;
    }
}
