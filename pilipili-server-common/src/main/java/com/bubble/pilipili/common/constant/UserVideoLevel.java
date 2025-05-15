/*
 * Copyright (c) 2025. Bubble
 */

package com.bubble.pilipili.common.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Bubble
 * @date 2025.05.15 18:05
 */
@Getter
@AllArgsConstructor
public enum UserVideoLevel {

    LEVEL_PUBLIC("PUBLIC"),
    LEVEL_USER("USER"),
    LEVEL_ADMIN("ADMIN"),
    ;

    private final String key;
}
