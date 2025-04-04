/*
 * Copyright (c) 2025. Bubble
 */

package com.bubble.pilipili.user.pojo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author Bubble
 * @date 2025.04.04 21:43
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class QueryUserRelaDTO implements Serializable {

    /**
     * 关注发起者uid
     */
    private Integer fromUid;
    /**
     * 被关注者uid
     */
    private Integer toUid;
    /**
     * 是否关注
     */
    private Boolean isFollow;
    /**
     * 是否特别关注
     */
    private Boolean isSpecial;
    /**
     * 是否互粉
     */
    private Boolean isMutual;

}
