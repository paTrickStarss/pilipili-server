/*
 * Copyright (c) 2025. Bubble
 */

package com.bubble.pilipili.user.pojo.dto;

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

    private Boolean special;
}
