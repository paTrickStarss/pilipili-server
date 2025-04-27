/*
 * Copyright (c) 2025. Bubble
 */

package com.bubble.pilipili.oss.controller;

import com.alibaba.fastjson2.JSON;
import com.bubble.pilipili.common.constant.AuthConstant;
import com.bubble.pilipili.common.http.Controller;
import com.bubble.pilipili.common.http.SimpleResponse;
import com.bubble.pilipili.common.pojo.JwtPayload;
import com.bubble.pilipili.feign.api.MQFeignAPI;
import com.bubble.pilipili.feign.api.OssFeignAPI;
import com.bubble.pilipili.feign.pojo.dto.OssUploadFileDTO;
import com.bubble.pilipili.oss.constant.OssFileDirectory;
import com.bubble.pilipili.oss.constant.UploadTaskStatus;
import com.bubble.pilipili.oss.pojo.dto.FFMpegTranscodingDTO;
import com.bubble.pilipili.oss.pojo.dto.OssAsyncUploadFileDTO;
import com.bubble.pilipili.feign.pojo.dto.OssTempSignDTO;
import com.bubble.pilipili.oss.pojo.dto.UploadTaskMessage;
import com.bubble.pilipili.oss.pojo.entity.VideoFileAnalyseResult;
import com.bubble.pilipili.oss.service.FFMpegService;
import com.bubble.pilipili.oss.service.OssService;
import com.bubble.pilipili.oss.service.TempMultipartFile;
import com.bubble.pilipili.oss.util.OssFileUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

/**
 * @author Bubble
 * @date 2025.03.21 16:10
 */
@Slf4j
@RestController
@RequestMapping("/api/oss")
@Tag(name = "OssController", description = "OSS文件上传下载管理接口")
public class OssController implements OssFeignAPI, Controller {

    @Autowired
    private OssService ossService;
    @Autowired
    private FFMpegService ffmpegService;

    @Autowired
    private WebSocketEndpoint webSocketEndpoint;

    @Autowired
    private MQFeignAPI mqFeignAPI;
    @Autowired
    private ThreadPoolTaskExecutor bubbleThreadPool;


    /**
     * 临时签名访问
     * @param objectNameList 待签名对象名列表
     * @return
     */
    @Operation(summary = "临时签名访问")
    @PostMapping("/tempSign")
    public SimpleResponse<OssTempSignDTO> getTempSigns(@RequestBody List<String> objectNameList) {
        OssTempSignDTO dto = ossService.getTempSigns(objectNameList);
        return SimpleResponse.success(dto);
    }

    /**
     * 上传视频
     * @param file
     * @return
     */
    @Operation(summary = "上传视频")
    @PostMapping("/video")
    public SimpleResponse<OssAsyncUploadFileDTO> uploadVideo(
            @RequestParam MultipartFile file,
            HttpServletRequest request
    ) {
        // 拷贝临时文件
        TempMultipartFile tempFile = TempMultipartFile.createTempFile(file);

        String jwtPayload = request.getHeader(AuthConstant.JWT_PAYLOAD_HEADER);
        JwtPayload payload = JSON.parseObject(jwtPayload, JwtPayload.class);
        String username = payload.getUsername();
        log.debug("username: {}", username);

        // taskId可以通过ws消息发送给客户端，也可以通过请求响应返回
        UploadTaskMessage message = new UploadTaskMessage();
        String taskId = UUID.randomUUID().toString();
        message.setTaskId(taskId);
        message.setUsername(username);
        message.setStatus(UploadTaskStatus.CREATED.ordinal());
        message.setProgress(0);
        message.setMsg("任务已创建！准备开始转码");
        message.setMsgTime(System.currentTimeMillis());
        webSocketEndpoint.sendSingleMessage(username, JSON.toJSONString(message));

//        return SimpleResponse.success("test prohibit");

        // 创建最终访问对象名
//        String objectName = OssFileUtil.getObjectFullPathName(file, OssFileDirectory.VIDEO_CONTENT.getValue());
        String objectName = OssFileUtil.generateHlsObjFullPathName(OssFileDirectory.VIDEO_CONTENT.getValue());
        // 视频转码后上传
        CompletableFuture<FFMpegTranscodingDTO> transcodingCF =
                CompletableFuture.supplyAsync(() -> {
                    try {
                        return ffmpegService.videoTranscoding(tempFile);
                    } catch (Exception e) {
                        log.error("videoTranscoding error, {}", e.getMessage());
//                        throw new RuntimeException(e);
                        return FFMpegTranscodingDTO.failure();
                    }
                }, bubbleThreadPool);

        // 后台创建OSS上传任务，请求接收完前端发送的文件后直接返回响应，最终访问路径为objectName
        CompletableFuture<OssUploadFileDTO> uploadCF =
                transcodingCF.thenApply(dto -> {
                    if (!dto.getSuccess()) {
                        return OssUploadFileDTO.failed("upload failed due to transcoding failed.");
                    }
                    String fileDirectory = dto.getOutputDirectory();
                    VideoFileAnalyseResult analyseResult = dto.getAnalyseResult();
                    // MQ更新视频信息
                    // 这里主要是为了更新视频时长信息，但实测发现前端上传视频时可以拿到时长信息，因此这一步可以省略
//                    SendVideoInfoReq req = new SendVideoInfoReq();
//                    req.setTaskId(taskId);
//                    req.setDuration(analyseResult.getDuration());
//                    mqFeignAPI.sendVideoInfo(req);

                    try {
                        return ossService.batchUploadHlsVideo(fileDirectory, objectName);
                    } catch (Exception e) {
                        log.error("asyncUploadVideo error, {}", e.getMessage());
//                        throw new RuntimeException(e);
                        return OssUploadFileDTO.failed("asyncUploadVideo error, " + e.getMessage());
                    }
                });
        uploadCF.thenAccept(dto -> {
            tempFile.delete();
            if (dto.getSuccess()) {
                // 上传完成，通知前端
                UploadTaskMessage resultMessage = new UploadTaskMessage();
                resultMessage.setTaskId(taskId);
                resultMessage.setUsername(username);
                resultMessage.setStatus(UploadTaskStatus.COMPLETED.ordinal());
                resultMessage.setProgress(100);
                resultMessage.setMsg("上传任务已完成！path: " + dto.getObjectName());
                resultMessage.setMsgTime(System.currentTimeMillis());
                webSocketEndpoint.sendSingleMessage(username, JSON.toJSONString(resultMessage));

                // 执行视频信息更新（contentUrl）
                // 没有必要更新，因为objectName可以先生成返回给前端去保存视频信息，等到视频文件上传OSS任务完成后自然就可以通过objectName访问了
//                SendVideoInfoReq req = new SendVideoInfoReq();
//                req.setTaskId(taskId);
//                req.setContentUrl(dto.getObjectName());
//                mqFeignAPI.sendVideoInfo(req);
            }
        });

        return SimpleResponse.success(new OssAsyncUploadFileDTO(taskId, objectName));
    }

    /**
     * 上传动态视频
     * @param file
     * @return
     */
    @Operation(summary = "上传动态视频")
    @PostMapping("/video/dynamic")
    public SimpleResponse<OssUploadFileDTO> uploadDynamicVideo(@RequestParam MultipartFile file) {
        String objectName = OssFileUtil.getObjectFullPathName(file, OssFileDirectory.VIDEO_DYNAMIC.getValue());
        OssUploadFileDTO dto = ossService.uploadDynamicVideo(file, objectName);
        return handleDTO(dto);
    }

    /**
     * 上传头像
     * @param file
     * @return
     */
    @Operation(summary = "上传头像")
    @PostMapping("/image/avatar")
    public SimpleResponse<OssUploadFileDTO> uploadAvatar(@RequestParam MultipartFile file) {
        OssUploadFileDTO dto = ossService.uploadAvatar(file);
        return handleDTO(dto);
    }

    /**
     * 上传视频封面
     * @param file
     * @return
     */
    @Operation(summary = "上传视频封面")
    @PostMapping("/image/cover/video")
    public SimpleResponse<OssUploadFileDTO> uploadVideoCover(@RequestParam MultipartFile file) {
        OssUploadFileDTO dto = ossService.uploadVideoCover(file);
        return handleDTO(dto);
    }

    /**
     * 上传收藏夹封面
     * @param file
     * @return
     */
    @Operation(summary = "上传收藏夹封面")
    @PostMapping("/image/cover/collection")
    public SimpleResponse<OssUploadFileDTO> uploadCollectionCover(@RequestParam MultipartFile file) {
        OssUploadFileDTO dto = ossService.uploadCollectionCover(file);
        return handleDTO(dto);
    }

    /**
     * 上传动态图片
     * @param file
     * @return
     */
    @Operation(summary = "上传动态图片")
    @PostMapping("/image/dynamic")
    public SimpleResponse<OssUploadFileDTO> uploadDynamicImage(@RequestParam MultipartFile file) {
        OssUploadFileDTO dto = ossService.uploadDynamicImage(file);
        return handleDTO(dto);
    }

    /**
     * 封装返回对象
     * @param dto
     * @return
     */
    private SimpleResponse<OssUploadFileDTO> handleDTO(OssUploadFileDTO dto) {
        if (dto.getSuccess()) {
            return SimpleResponse.success(dto);
        } else {
            return SimpleResponse.failed(dto.getMsg());
        }
    }
}
