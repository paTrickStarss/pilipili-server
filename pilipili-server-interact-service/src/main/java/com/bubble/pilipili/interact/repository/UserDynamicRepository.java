/*
 * Copyright (c) 2025. Bubble
 */

package com.bubble.pilipili.interact.repository;

import com.bubble.pilipili.interact.pojo.dto.QueryDynamicStatsDTO;
import com.bubble.pilipili.interact.pojo.entity.UserDynamic;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Bubble
 * @date 2025.03.04 11:52
 */
@Repository
public interface UserDynamicRepository {


    Boolean saveUserDynamic(UserDynamic userDynamic);

    QueryDynamicStatsDTO getDynamicStats(Integer did);
    List<QueryDynamicStatsDTO> getDynamicStats(List<Integer> didList);

}
