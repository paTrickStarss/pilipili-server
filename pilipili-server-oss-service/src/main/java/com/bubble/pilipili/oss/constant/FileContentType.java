/*
 * Copyright (c) 2025. Bubble
 */

package com.bubble.pilipili.oss.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Bubble
 * @date 2025.03.21 15:09
 */
@Getter
@AllArgsConstructor
public enum FileContentType {

    VIDEO("video"),
    IMAGE("image"),
    ;

    private final String prefix;
}
