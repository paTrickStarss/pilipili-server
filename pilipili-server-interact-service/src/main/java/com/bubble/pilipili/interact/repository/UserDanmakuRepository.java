/*
 * Copyright (c) 2025. Bubble
 */

package com.bubble.pilipili.interact.repository;

import com.bubble.pilipili.interact.pojo.dto.QueryDanmakuStatsDTO;
import com.bubble.pilipili.interact.pojo.entity.UserDanmaku;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Bubble
 * @date 2025.03.06 14:32
 */
@Repository
public interface UserDanmakuRepository {

    Boolean saveUserDanmaku(UserDanmaku userDanmaku);

    List<QueryDanmakuStatsDTO> getDanmakuStats(List<Integer> danmakuIdList);
}
