/*
 * Copyright (c) 2025. Bubble
 */

package com.bubble.pilipili.oss.pojo.entity;

import com.bubble.pilipili.oss.constant.FFmpegTranscodeQuantityLevel;
import com.bubble.pilipili.oss.constant.VideoOrientation;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Bubble
 * @date 2025.04.02 16:29
 */
@Data
@NoArgsConstructor
public class VideoFileAnalyseResult {

    private Long size;
    private Long duration;
    private Integer width;
    private Integer height;
    private VideoOrientation orientation;
    private Long totalBitrate;
    private Long videoBitrate;
    private Long audioBitrate;

    private FFmpegTranscodeQuantityLevel level;
}
