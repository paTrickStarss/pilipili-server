/*
 * Copyright (c) 2025. Bubble
 */

package com.bubble.pilipili.interact.repository;

import com.bubble.pilipili.interact.pojo.entity.UserComment;
import org.springframework.stereotype.Repository;

/**
 * @author Bubble
 * @date 2025.03.05 21:24
 */
@Repository
public interface UserCommentRepository {

    Boolean saveUserComment(UserComment userComment);

//    QueryCommentStatsDTO getCommentStats(Integer cid);
//    List<QueryCommentStatsDTO> getCommentStats(List<Integer> cidList);
}
