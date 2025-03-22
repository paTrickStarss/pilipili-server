/*
 * Copyright (c) 2025. Bubble
 */

package com.bubble.pilipili.oss.controller;

import com.alibaba.fastjson2.JSON;
import com.bubble.pilipili.common.constant.AuthConstant;
import com.bubble.pilipili.common.http.Controller;
import com.bubble.pilipili.common.http.SimpleResponse;
import com.bubble.pilipili.common.pojo.JwtPayload;
import com.bubble.pilipili.feign.pojo.dto.OssUploadFileDTO;
import com.bubble.pilipili.oss.service.OssService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Bubble
 * @date 2025.03.21 16:10
 */
@Slf4j
@RestController
@RequestMapping("/api/oss")
@Tag(name = "OssController", description = "OSS文件上传下载管理接口")
public class OssController implements Controller {

    @Autowired
    private OssService ossService;

    @Autowired
    private WebSocketEndpoint webSocketEndpoint;

    /**
     * 上传视频
     * @param file
     * @return
     */
    @Operation(summary = "上传视频")
    @PostMapping("/video")
    public SimpleResponse<OssUploadFileDTO> uploadVideo(
            @RequestParam MultipartFile file, HttpServletRequest request
    ) {
        String jwtPayload = request.getHeader(AuthConstant.JWT_PAYLOAD_HEADER);
        log.debug("jwtPayload: {}", jwtPayload);

        JwtPayload payload = JSON.parseObject(jwtPayload, JwtPayload.class);
        log.debug("payload: {}", payload);
        String username = payload.getUsername();
        log.debug("username: {}", username);

        webSocketEndpoint.sendSingleMessage(username,
                "Server hava received a video from you. Video File name: " + file.getOriginalFilename());

        return SimpleResponse.failed("test prohibit");
//        OssUploadFileDTO dto = ossService.uploadVideo(file);
//        return handleDTO(dto);
    }

    /**
     * 上传动态视频
     * @param file
     * @return
     */
    @Operation(summary = "上传动态视频")
    @PostMapping("/video/dynamic")
    public SimpleResponse<OssUploadFileDTO> uploadDynamicVideo(@RequestParam MultipartFile file) {
        OssUploadFileDTO dto = ossService.uploadDynamicVideo(file);
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
