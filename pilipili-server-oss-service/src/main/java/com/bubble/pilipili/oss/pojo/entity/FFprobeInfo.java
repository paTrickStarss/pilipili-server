/*
 * Copyright (c) 2025. Bubble
 */

package com.bubble.pilipili.oss.pojo.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author Bubble
 * @date 2025.04.01 15:07
 */
@Data
@NoArgsConstructor
public class FFprobeInfo {

    private List<FFprobeStream> streams;
    private FFprobeFormat format;
}
