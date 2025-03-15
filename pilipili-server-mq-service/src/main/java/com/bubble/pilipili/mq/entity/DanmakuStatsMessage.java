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
    private Integer danmakuId;
    private Long favorCount;
    private Long dewCount;
}
