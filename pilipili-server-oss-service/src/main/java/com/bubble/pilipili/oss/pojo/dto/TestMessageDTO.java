/*
 * Copyright (c) 2025. Bubble
 */

package com.bubble.pilipili.oss.pojo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author Bubble
 * @date 2025.03.21 21:17
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TestMessageDTO implements Serializable {

    private String msg;
    private Integer count;
    private LocalDateTime serverTime;
}
