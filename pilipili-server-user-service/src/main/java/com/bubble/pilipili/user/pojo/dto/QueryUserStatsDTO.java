/*
 * Copyright (c) 2025. Bubble
 */

package com.bubble.pilipili.user.pojo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Bubble
 * @date 2025.03.07 15:48
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class QueryUserStatsDTO {

    private Integer uid;
    private Long followerCount = 0L;
    private Long fansCount = 0L;
}
