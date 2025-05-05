/*
 * Copyright (c) 2025. Bubble
 */

package com.bubble.pilipili.stats.repository;

import com.bubble.pilipili.common.repository.StatsEntityRepository;
import com.bubble.pilipili.common.pojo.UserStats;
import org.springframework.stereotype.Repository;

/**
 * @author Bubble
 * @date 2025.03.19 23:02
 */
@Repository
public interface UserStatsRepository extends StatsEntityRepository<UserStats> {
}
