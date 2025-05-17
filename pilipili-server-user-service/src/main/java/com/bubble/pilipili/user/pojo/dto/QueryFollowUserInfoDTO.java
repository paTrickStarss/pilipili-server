/*
 * Copyright (c) 2025. Bubble
 */

package com.bubble.pilipili.user.pojo.dto;

import com.bubble.pilipili.feign.pojo.dto.QueryUserInfoDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @author Bubble
 * @date 2025.03.07 17:59
 */
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class QueryFollowUserInfoDTO extends QueryUserInfoDTO {

    /**
     * 特别关注
     */
    private Boolean special;
    /**
     * 互粉
     */
    private Boolean isMutual;
}
