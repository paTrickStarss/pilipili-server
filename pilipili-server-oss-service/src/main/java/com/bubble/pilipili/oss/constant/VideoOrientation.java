/*
 * Copyright (c) 2025. Bubble
 */

package com.bubble.pilipili.oss.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 视频方向枚举
 * @author Bubble
 * @date 2025.04.02 18:04
 */
@Getter
@AllArgsConstructor
public enum VideoOrientation {

    LANDSCAPE("Landscape", "横屏"),
    PORTRAIT("Portrait", "竖屏"),
    SQUARE("Square", "正方形"),
    ;


    private final String name;
    private final String desc;
}
