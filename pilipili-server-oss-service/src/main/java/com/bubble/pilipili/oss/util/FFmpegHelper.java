/*
 * Copyright (c) 2025. Bubble
 */

package com.bubble.pilipili.oss.util;

import com.alibaba.fastjson2.JSON;
import com.bubble.pilipili.oss.constant.FFmpegTranscodeQuantityLevel;
import com.bubble.pilipili.oss.pojo.entity.CommandResult;
import com.bubble.pilipili.oss.pojo.entity.FFprobeInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * @author Bubble
 * @date 2025.04.01 15:05
 */
@Component
@Slf4j
public class FFmpegHelper {

    public static final String BASE_PATH = "T:\\Work\\Java\\PiliPili_Project\\test\\";
    public static final String FFMPEG = "ffmpeg";
    public static final String FFPROBE = "ffprobe";
    public static final String VIDEO_TRANSCODING_HLS_BAT = "T:\\Work\\Java\\PiliPili_Project\\test\\video_transcoding_hls.bat";

    /**
     * 检查视频信息
     * @param input
     * @return
     */
    public FFprobeInfo getFFprobeInfo(String input) {
        String args = "-loglevel error -print_format json -show_format -show_streams -i " + BASE_PATH + input;
        CommandResult result = doCommand(FFPROBE, args);
        if (result.getSuccess()) {
            return JSON.parseObject(result.getOutput(), FFprobeInfo.class);
        }

        return null;
    }

    /**
     * 将视频转码为多档清晰度的HLS格式
     * @param input 源视频相对路径，参考{@code BASE_PATH}
     * @param level 视频质量等级，参考{@code FFmpegTranscodeQuantityLevel}
     */
    public void videoTranscodingToHLS(String input, FFmpegTranscodeQuantityLevel level) {
        // 将视频转码为7档分辨率的hls格式视频 需要按高到低排序
        StringBuilder ab = new StringBuilder();
        ab.append(BASE_PATH).append(input).append(" ");
        ab.append(BASE_PATH).append(OssFileUtil.getFileNameWithoutExtension(input)).append(" ");
        ab.append(level.getLevel());

        CommandResult result = doCommand(VIDEO_TRANSCODING_HLS_BAT, ab.toString());
        if (result.getSuccess()) {
            log.info("videoTranscodingToHLS success, elapsedTime: {} ms", result.getElapsedTime());
        } else {
            log.warn("videoTranscodingToHLS error, {}\nelapsedTime: {} ms",
                    result.getOutput(), result.getElapsedTime());
        }
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


    public static void main(String[] args) {
        FFmpegHelper helper = new FFmpegHelper();
//        FFprobeInfo fFprobeInfo = helper.getFFprobeInfo("input-1.mp4");
//        System.out.println(fFprobeInfo);

        helper.videoTranscodingToHLS("input-1.mp4", FFmpegTranscodeQuantityLevel.V_1080P_HIGH_BITRATE);
    }
}
