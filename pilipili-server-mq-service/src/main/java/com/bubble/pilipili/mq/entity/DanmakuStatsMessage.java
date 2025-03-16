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
public class DanmakuStatsMessage extends StatsMessage {
    private Integer danmakuId = 0;
    private Long favorCount = 0L;
    private Long dewCount = 0L;

    public Integer getDanmakuId() {
        return danmakuId == null ? 0 : danmakuId;
    }

    public Long getFavorCount() {
        return favorCount == null ? 0 : favorCount;
    }

    public Long getDewCount() {
        return dewCount == null ? 0 : dewCount;
    }
}
