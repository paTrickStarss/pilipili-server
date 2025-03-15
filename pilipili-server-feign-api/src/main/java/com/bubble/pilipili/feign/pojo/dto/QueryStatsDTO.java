/*
 * Copyright (c) 2025. Bubble
 */

package com.bubble.pilipili.feign.pojo.dto;

import com.bubble.pilipili.common.pojo.StatsEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Map;

/**
 * @author Bubble
 * @date 2025.03.15 20:27
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class QueryStatsDTO<T extends StatsEntity> implements Serializable {
    private Map<Integer, T> statsMap;
}
