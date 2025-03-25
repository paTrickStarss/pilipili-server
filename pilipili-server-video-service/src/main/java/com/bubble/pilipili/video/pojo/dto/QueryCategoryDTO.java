/*
 * Copyright (c) 2025. Bubble
 */

package com.bubble.pilipili.video.pojo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author Bubble
 * @date 2025.03.25 15:34
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class QueryCategoryDTO implements Serializable {
    private Integer id;
    private String name;
}
