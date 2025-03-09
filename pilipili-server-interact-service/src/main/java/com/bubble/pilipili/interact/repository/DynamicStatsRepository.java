/*
 * Copyright (c) 2025. Bubble
 */

package com.bubble.pilipili.interact.repository;

import com.bubble.pilipili.common.repository.StatsEntityRepository;
import com.bubble.pilipili.interact.pojo.entity.DynamicStats;
import org.springframework.stereotype.Repository;

/**
 * @author Bubble
 * @date 2025.03.09 20:51
 */
@Repository
public interface DynamicStatsRepository extends StatsEntityRepository<DynamicStats> {
}
