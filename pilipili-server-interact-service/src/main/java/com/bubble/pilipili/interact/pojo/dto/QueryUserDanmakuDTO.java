/*
 * Copyright (c) 2025. Bubble
 */

package com.bubble.pilipili.interact.pojo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author Bubble
 * @date 2025.03.29 22:36
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class QueryUserDanmakuDTO implements Serializable {

    private Integer danmakuId;
    private Integer uid;
    private Integer favor;
    private Integer dew;
}
