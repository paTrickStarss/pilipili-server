/*
 * Copyright (c) 2025. Bubble
 */

package com.bubble.pilipili.oss.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * FFmpeg转码质量等级
 * @author Bubble
 * @date 2025.04.01 18:35
 */
@Getter
@AllArgsConstructor
public enum FFmpegTranscodeQuantityLevel {

    V_360P(3),
    V_480P(4),
    V_720P(5),
    V_1080P(6),
    V_1080P_HIGH_BITRATE(7),
    V_4K(8),
    ;

    private final Integer level;

}
