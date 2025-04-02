/*
 * Copyright (c) 2025. Bubble
 */

package com.bubble.pilipili.oss.service.impl;

import com.bubble.pilipili.oss.pojo.dto.FFMpegTranscodingDTO;
import com.bubble.pilipili.oss.pojo.entity.VideoFileAnalyseResult;
import com.bubble.pilipili.oss.service.FFMpegService;
import com.bubble.pilipili.oss.service.TempMultipartFile;
import com.bubble.pilipili.oss.util.FFmpegHelper;
import com.bubble.pilipili.oss.util.OssFileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author Bubble
 * @date 2025.04.02 15:45
 */
@Service
public class FFMpegServiceImpl implements FFMpegService {

    @Autowired
    private FFmpegHelper ffmpegHelper;


    /**
     * 视频转码
     *
     * @param file
     * @return
     */
    @Override
    public FFMpegTranscodingDTO videoTranscoding(TempMultipartFile file) {
        // 获取文件路径
        String path = file.getAbsolutePath();
        // 生成输出目录
        String outputDirectory = OssFileUtil.getFileNameWithoutExtension(path);

        VideoFileAnalyseResult analyseResult = ffmpegHelper.analyseVideoFile(path);
        // 开始转码
        boolean success = ffmpegHelper.videoTranscodingToHLS(path, outputDirectory, analyseResult);

        // 返回结果
        if (success) {
            return FFMpegTranscodingDTO.success(outputDirectory, analyseResult);
        }
        return FFMpegTranscodingDTO.failure();
    }
}
