/*
 * Copyright (c) 2025. Bubble
 */

package com.bubble.pilipili.oss.service;

import com.bubble.pilipili.oss.pojo.dto.FFMpegTranscodingDTO;
import org.springframework.stereotype.Service;

/**
 * @author Bubble
 * @date 2025.04.02 15:36
 */
@Service("ffmpegService")
public interface FFMpegService {

    /**
     * 视频转码
     * @param file
     * @return
     */
    FFMpegTranscodingDTO videoTranscoding(TempMultipartFile file);
}
