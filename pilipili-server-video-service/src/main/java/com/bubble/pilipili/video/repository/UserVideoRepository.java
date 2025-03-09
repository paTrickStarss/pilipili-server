/*
 * Copyright (c) 2025. Bubble
 */

package com.bubble.pilipili.video.repository;

import com.bubble.pilipili.video.pojo.dto.QueryVideoStatsDTO;
import com.bubble.pilipili.video.pojo.entity.UserVideo;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Bubble
 * @date 2025.03.07 21:35
 */
@Repository
public interface UserVideoRepository {

    Boolean saveUserVideo(UserVideo userVideo);

    QueryVideoStatsDTO countVideoStats(Integer vid);

    List<QueryVideoStatsDTO> countVideoStats(List<Integer> vidList);
}
