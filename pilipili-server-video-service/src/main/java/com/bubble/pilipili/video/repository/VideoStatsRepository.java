/*
 * Copyright (c) 2025. Bubble
 */

package com.bubble.pilipili.video.repository;

import com.bubble.pilipili.common.repository.StatsEntityRepository;
import com.bubble.pilipili.video.pojo.entity.VideoStats;
import org.springframework.stereotype.Repository;

/**
 * @author Bubble
 * @date 2025.03.09 14:42
 */
@Repository
public interface VideoStatsRepository extends StatsEntityRepository<VideoStats> {
}
