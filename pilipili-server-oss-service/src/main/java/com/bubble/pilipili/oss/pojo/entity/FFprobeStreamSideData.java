/*
 * Copyright (c) 2025. Bubble
 */

package com.bubble.pilipili.oss.pojo.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Bubble
 * @date 2025.04.02 18:38
 */
@Data
@NoArgsConstructor
public class FFprobeStreamSideData {

    private String side_data_type;
    private String displaymatrix;
    private Integer rotation;
}
