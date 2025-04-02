/*
 * Copyright (c) 2025. Bubble
 */

package com.bubble.pilipili.oss.util;

import com.alibaba.fastjson2.JSON;
import com.bubble.pilipili.common.exception.FFMpegException;
import com.bubble.pilipili.common.util.ListUtil;
import com.bubble.pilipili.common.util.StringUtil;
import com.bubble.pilipili.oss.constant.FFmpegTranscodeQuantityLevel;
import com.bubble.pilipili.oss.constant.MajorBrand;
import com.bubble.pilipili.oss.constant.VideoOrientation;
import com.bubble.pilipili.oss.pojo.entity.*;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Objects;

/**
 * @author Bubble
 * @date 2025.04.01 15:05
 */
@Component
@Slf4j
public class FFmpegHelper {

    public static final String DIR_DIVIDER = "\\";
    /**
     * 命令工作工作目录（临时文件目录）
     */
    public static final String BASE_PATH = "T:\\tmp\\oss\\";
    public static final String FFMPEG = "ffmpeg";
    public static final String FFPROBE = "ffprobe";
    /**
     * 视频转码脚本路径
     */
    public static final String VIDEO_TRANSCODING_HLS_BAT = "T:\\Work\\Java\\PiliPili_Project\\test\\video_transcoding_hls.bat";
    public static final String HLS_MASTER_PL_NAME = "master.m3u8";
    /**
     * 高码率门槛：6000kbps
     */
    public static final Long HIGH_BITRATE_THRESHOLD = 6000000L;

    /**
     * 检查视频信息
     * @param input
     * @return
     */
    public FFprobeInfo getFFprobeInfo(String input) {
        String args = "-loglevel error -print_format json -show_format -show_streams -i " + input;
        CommandResult result = doCommand(FFPROBE, args);
        if (result.getSuccess()) {
            return JSON.parseObject(result.getOutput(), FFprobeInfo.class);
        }

        return null;
    }

    /**
     * 分析视频文件
     * @param input
     * @return
     */
    public VideoFileAnalyseResult analyseVideoFile(String input) {
        FFprobeInfo info = getFFprobeInfo(input);
        if (info == null) {
            throw new FFMpegException("FFprobe info is null");
        }
        String size = info.getFormat().getSize();
        if (StringUtil.isEmpty(size)) {
            throw new FFMpegException("FFprobe size is empty");
        }
        long sizeLong = Long.parseLong(size);
        // 整个视频文件的比特率（Video + Audio）
        String totalBitRate = info.getFormat().getBit_rate();
        if (StringUtil.isEmpty(totalBitRate)) {
            throw new FFMpegException("FFprobe info videoBitRate is empty");
        }
        long totalBitRateLong = Long.parseLong(totalBitRate);

        String duration = info.getFormat().getDuration();
        if (StringUtil.isEmpty(duration)) {
            throw new FFMpegException("FFprobe duration is empty");
        }
        long durationLong = ((long) Math.ceil(Double.parseDouble(duration)));

        List<FFprobeStream> streams = info.getStreams();
        if (ListUtil.isEmpty(streams)) {
            throw new FFMpegException("FFprobe info streams is empty");
        }
        if (streams.size() < 2) {
            throw new FFMpegException("FFprobe info streams is less than 2");
        }

        boolean isMov = false;
        int rotation = 0;
        // 针对MOV格式 获取其旋转角信息
        if (Objects.equals(info.getFormat().getTags().getMajor_brand(), MajorBrand.MOV.getName())) {
            isMov = true;
            List<FFprobeStreamSideData> sideDataList = info.getStreams().get(0).getSide_data_list();
            if (ListUtil.isNotEmpty(sideDataList)) {
                Integer rotationRaw = sideDataList.get(0).getRotation();
                rotation = rotationRaw == null ? 0 : rotationRaw;
            }
        }

        return getAnalyseResult(streams, sizeLong, durationLong, totalBitRateLong, isMov, rotation);
    }

    /**
     * 获取视频分析结果
     * @param streams
     * @param durationLong
     * @param totalBitRateLong
     * @return
     */
    @NotNull
    private VideoFileAnalyseResult getAnalyseResult(List<FFprobeStream> streams, long sizeLong, long durationLong, long totalBitRateLong, boolean isMov, int rotation) {
        FFprobeStream videoStream = streams.get(0);
        // 视频流的比特率
        String videoBitRate = videoStream.getBit_rate();
        if (StringUtil.isEmpty(videoBitRate)) {
            throw new FFMpegException("FFprobe info videoBitRate is empty");
        }
        long videoBitRateLong = Long.parseLong(videoBitRate);
        // 视频帧高
        Integer height = videoStream.getHeight();
        if (height == null || height <= 0) {
            throw new FFMpegException("FFprobe info height is empty");
        }
        // 视频帧宽
        Integer width = videoStream.getWidth();
        if (width == null || width <= 0) {
            throw new FFMpegException("FFprobe info width is empty");
        }

        String audioBitRate = streams.get(1).getBit_rate();
        if (StringUtil.isEmpty(audioBitRate)) {
            throw new FFMpegException("FFprobe info audioBitRate is empty");
        }
        long audioBitRateLong = Long.parseLong(audioBitRate);

        // mov视频竖屏模式依然是width > height，采用旋转90度实现竖屏播放，所以这里会误判为横屏（Landscape）
        // 已解决
        VideoOrientation orientation;
        int min = Math.min(height, width);
        if (isMov) {
            if (Math.abs(rotation) == 90) {
                orientation = VideoOrientation.PORTRAIT;
            } else {
                orientation = VideoOrientation.LANDSCAPE;
            }
        } else {
            if (width.equals(height)) {
                orientation = VideoOrientation.SQUARE;
            } else if (min == height) {
                orientation = VideoOrientation.LANDSCAPE;
            } else {
                orientation = VideoOrientation.PORTRAIT;
            }
        }

        FFmpegTranscodeQuantityLevel level = getTranscodeQuantityLevel(min, videoBitRateLong);

        VideoFileAnalyseResult result = new VideoFileAnalyseResult();
        result.setSize(sizeLong);
        result.setDuration(durationLong);
        result.setWidth(width);
        result.setHeight(height);
        result.setOrientation(orientation);
        result.setTotalBitrate(totalBitRateLong);
        result.setVideoBitrate(videoBitRateLong);
        result.setAudioBitrate(audioBitRateLong);
        result.setLevel(level);
        return result;
    }


    /**
     * 根据帧高（横屏）或帧宽（竖屏）和码率判断视频转码质量等级
     * @param height
     * @param videoBitRateLong
     * @return
     */
    @NotNull
    private FFmpegTranscodeQuantityLevel getTranscodeQuantityLevel(Integer height, long videoBitRateLong) {
        FFmpegTranscodeQuantityLevel level;
        // 4K视频码率需要达到1080P的高码率门槛才支持转码最高质量4K，否则最高质量为1080p
        if (height >= 2160 && videoBitRateLong >= HIGH_BITRATE_THRESHOLD) {
            level = FFmpegTranscodeQuantityLevel.V_4K;
        } else if (height >= 1080) {
            // 码率高于高码率门槛，才有高码率版本
            if (videoBitRateLong >= HIGH_BITRATE_THRESHOLD) {
                level = FFmpegTranscodeQuantityLevel.V_1080P_HIGH_BITRATE;
            } else {
                level = FFmpegTranscodeQuantityLevel.V_1080P;
            }
        } else if (height >= 720) {
            level = FFmpegTranscodeQuantityLevel.V_720P;
        } else if (height >= 480) {
            level = FFmpegTranscodeQuantityLevel.V_480P;
        } else if (height >= 360) {
            level = FFmpegTranscodeQuantityLevel.V_360P;
        } else {
            // 低于360p的视为360p
            level = FFmpegTranscodeQuantityLevel.V_360P;
        }
        return level;
    }

    /**
     * 将视频转码为多档清晰度的HLS格式
     * @param input 源视频路径
     * @param outputDirectory 转码结果目录路径
     * @param analyseResult 视频分析结果
     */
    public boolean videoTranscodingToHLS(String input, String outputDirectory, VideoFileAnalyseResult analyseResult) {
        FFmpegTranscodeQuantityLevel level = analyseResult.getLevel();
        return videoTranscodingToHLS(input, outputDirectory, level);
    }

    /**
     * 将视频转码为多档清晰度的HLS格式
     * @param input 源视频路径
     * @param outputDirectory 转码结果目录路径
     * @return
     */
    public boolean videoTranscodingToHLS(String input, String outputDirectory) {
        VideoFileAnalyseResult analyseResult = analyseVideoFile(input);
        return videoTranscodingToHLS(input, outputDirectory, analyseResult);
    }

    /**
     * 将视频转码为多档清晰度的HLS格式
     * @param input 源视频路径
     * @param outputDirectory 转码结果目录路径
     * @param level 视频质量等级，参考{@code FFmpegTranscodeQuantityLevel}
     */
    public boolean videoTranscodingToHLS(String input, String outputDirectory, FFmpegTranscodeQuantityLevel level) {
        // 将视频转码为多档质量（由level决定）的hls格式视频，按高到低排序
        StringBuilder ab = new StringBuilder();
        ab
//                .append(BASE_PATH)
                .append(input).append(" ")
//                .append(BASE_PATH)
                .append(outputDirectory).append(" ")
                .append(level.getLevel());

        CommandResult result = doCommand(VIDEO_TRANSCODING_HLS_BAT, ab.toString());
        if (result.getSuccess()) {
            log.info("videoTranscodingToHLS success, elapsedTime: {} ms", result.getElapsedTime());
        } else {
            log.warn("videoTranscodingToHLS error, {}\nelapsedTime: {} ms",
                    result.getOutput(), result.getElapsedTime());
        }
        return result.getSuccess();
    }


    /**
     * 执行命令
     * @param target
     * @param args
     * @return exitCode 0表示成功 非0则执行异常
     */
    private CommandResult doCommand(String target, String args) {

        String command = target + " " + args;

        StringBuilder output = new StringBuilder();
        ProcessBuilder processBuilder = new ProcessBuilder(command.split(" "));
        processBuilder.redirectErrorStream(true);

        try {
            log.info("Executing command: {}", command);
            long l1 = System.currentTimeMillis();
            Process process = processBuilder.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

            String line;
            while ((line = reader.readLine()) != null) {
//                System.out.println(line);
                output.append(line).append("\n");
            }
            int exitCode = process.waitFor();
            long elapsedTime = System.currentTimeMillis() - l1;
            if (exitCode != 0) {
                output.append("Error Exit code: ").append(exitCode).append("\n");
                return CommandResult.failure(output.toString(), elapsedTime);
            }
            return CommandResult.success(output.toString(), elapsedTime);

        } catch (IOException | InterruptedException e) {
            output.append("Command process error: ").append(e.getMessage()).append("\n");
            log.error("Command process error: {}", output);
            throw new RuntimeException(e);
        }
    }


    public static void main(String[] args) throws IOException {
        FFmpegHelper helper = new FFmpegHelper();
//        FFprobeInfo fFprobeInfo = helper.getFFprobeInfo("input-1.mp4");
//        System.out.println(fFprobeInfo);

//        helper.videoTranscodingToHLS("input-1.mp4", FFmpegTranscodeQuantityLevel.V_1080P_HIGH_BITRATE);
//        long startTime = System.currentTimeMillis();
//        VideoFileAnalyseResult result = helper.analyseVideoFile("IMG_2851.MOV");
//        System.out.println(result);
//        System.out.println("elapseTime: " + (System.currentTimeMillis() - startTime) + " ms");

//        File file = new File(BASE_PATH + "IMG_2851.MOV");
//        System.out.println(file.getName());
//        System.out.println(file.getPath());
//        System.out.println(file.getAbsolutePath());
//        System.out.println(file.getCanonicalPath());
    }
}
