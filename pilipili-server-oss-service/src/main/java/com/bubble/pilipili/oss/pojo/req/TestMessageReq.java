/*
 * Copyright (c) 2025. Bubble
 */

package com.bubble.pilipili.oss.pojo.req;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author Bubble
 * @date 2025.03.21 21:15
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TestMessageReq implements Serializable {

    private Integer id;
    private String message;
    private LocalDateTime createTime;
}
