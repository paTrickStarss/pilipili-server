/*
 * Copyright (c) 2025. Bubble
 */

package com.bubble.pilipili.oss.pojo.dto;

import com.bubble.pilipili.oss.pojo.entity.VideoFileAnalyseResult;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author Bubble
 * @date 2025.04.02 15:38
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FFMpegTranscodingDTO implements Serializable {


    private Boolean success;
    /**
     * 转码完成视频存放目录
     */
    private String outputDirectory;
    private VideoFileAnalyseResult analyseResult;
//    private String msg;

    public static FFMpegTranscodingDTO success(String outputDirectory, VideoFileAnalyseResult analyseResult) {
        FFMpegTranscodingDTO dto = new FFMpegTranscodingDTO();
        dto.success = true;
        dto.outputDirectory = outputDirectory;
        dto.analyseResult = analyseResult;
//        dto.msg = null;
        return dto;
    }

    public static FFMpegTranscodingDTO failure() {
        FFMpegTranscodingDTO dto = new FFMpegTranscodingDTO();
        dto.success = false;
//        dto.msg = msg;
        return dto;
    }
}
