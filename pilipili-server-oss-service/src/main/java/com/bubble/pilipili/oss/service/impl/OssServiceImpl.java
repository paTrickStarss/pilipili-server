/*
 * Copyright (c) 2025. Bubble
 */

package com.bubble.pilipili.oss.service.impl;

import com.bubble.pilipili.common.exception.UtilityException;
import com.bubble.pilipili.common.util.ListUtil;
import com.bubble.pilipili.feign.pojo.dto.OssTempSignDTO;
import com.bubble.pilipili.feign.pojo.dto.OssUploadFileDTO;
import com.bubble.pilipili.oss.constant.FileContentType;
import com.bubble.pilipili.oss.constant.OssFileDirectory;
import com.bubble.pilipili.oss.service.OssService;
import com.bubble.pilipili.oss.util.FFmpegHelper;
import com.bubble.pilipili.oss.util.OssFileUtil;
import com.bubble.pilipili.oss.util.OssUploadHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;

/**
 * @author Bubble
 * @date 2025.03.21 14:19
 */
@Slf4j
@Service
public class OssServiceImpl implements OssService {

    @Autowired
    private OssUploadHelper ossUploadHelper;

    @Autowired
    @Qualifier("bubbleThreadPool")
    private ThreadPoolTaskExecutor bubbleThreadPool;

    /**
     * 上传视频
     * @param file
     * @return
     */
    @Override
    public OssUploadFileDTO uploadVideo(MultipartFile file, String objectName) {
        if (checkContentType(file, FileContentType.VIDEO)) {
            return contentTypeFallback(FileContentType.VIDEO);
        }
        return partUpload(file, objectName);
    }

    /**
     * 批量上传Hls视频
     *
     * @param fileDirectory
     * @param objectName OSS上访问 主播放列表（master.m3u8）的对象名
     * @return
     */
    @Override
    public OssUploadFileDTO batchUploadHlsVideo(String fileDirectory, String objectName) {
        File directory = new File(fileDirectory);
        // 目录不存在
        if (!directory.exists()) {
            log.error("directory [{}] does not exist", fileDirectory);
            return OssUploadFileDTO.failed("fileDirectory does not exist");
        }
        // 目标文件不是目录
        if (!directory.isDirectory()) {
            log.error("file [{}] is not a directory", fileDirectory);
            return OssUploadFileDTO.failed("fileDirectory is not a directory");
        }

        // 获取 主播放列表文件、各个level的目录
        File masterPlFile = null;
        List<File> levelDirList = new ArrayList<>();
        File[] files = directory.listFiles();
        // 目录下没有文件
        if (files == null || files.length == 0) {
            log.error("directory [{}] does not contain any files", fileDirectory);
            return OssUploadFileDTO.failed("directory does not contain any files");
        }

        for (File file : files) {
            if (file.isDirectory()) {
                levelDirList.add(file);
            } else {
                if (file.getName().equals(FFmpegHelper.HLS_MASTER_PL_NAME)) {
                    masterPlFile = file;
                }
            }
        }

        // 主播放列表文件找不到
        if (masterPlFile == null) {
            log.error("master play list file does not exist on directory [{}]", directory.getAbsolutePath());
            return OssUploadFileDTO.failed("master play list file does not exist");
        }
        // level目录找不到
        if (levelDirList.isEmpty()) {
            log.error("directory [{}] does not contain any level directory", fileDirectory);
            return OssUploadFileDTO.failed("directory does not contain any level directory");
        }

        // todo: 上传level目录
        boolean uploadLevelSuccess = uploadHlsLevelDir(levelDirList, objectName);

        // todo: 上传master（主播放列表）文件
        boolean uploadMasterPlSuccess = uploadHlsMasterPlFile(masterPlFile, objectName);


        return OssUploadFileDTO.success(objectName);
    }

    /**
     * 上传level目录
     * @param levelDirList level目录列表
     * @param rootObjectName level
     * @return
     */
    private boolean uploadHlsLevelDir(List<File> levelDirList, String rootObjectName) {


        return true;
    }

    /**
     * 上传master（主播放列表）文件
     * @param masterPlFile
     * @param objectName
     * @return
     */
    private boolean uploadHlsMasterPlFile(File masterPlFile, String objectName) {


        return true;
    }

    /**
     * 异步上传视频
     * @param file
     * @return
     */
    @Async("bubbleThreadPool")
    @Override
    public Future<OssUploadFileDTO> asyncUploadVideo(MultipartFile file, String objectName) {
        OssUploadFileDTO dto = uploadVideo(file, objectName);
        return new AsyncResult<>(dto);
    }

    /**
     * 上传动态视频
     *
     * @param file
     * @return
     */
    @Override
    public OssUploadFileDTO uploadDynamicVideo(MultipartFile file, String objectName) {
        return uploadVideo(file, objectName);
    }

    /**
     * 上传视频封面
     * @param file
     * @return
     */
    @Override
    public OssUploadFileDTO uploadVideoCover(MultipartFile file) {
        return uploadImage(file, OssFileDirectory.IMAGE_VIDEO_COVER);
    }
    /**
     * 上传头像
     * @param file
     * @return
     */
    @Override
    public OssUploadFileDTO uploadAvatar(MultipartFile file) {
        return uploadImage(file, OssFileDirectory.IMAGE_AVATAR);
    }

    /**
     * 上传头像
     *
     * @param file
     * @return
     */
    @Override
    public OssUploadFileDTO uploadCollectionCover(MultipartFile file) {
        return uploadImage(file, OssFileDirectory.IMAGE_COLLECTION_COVER);
    }

    /**
     * 上传头像
     *
     * @param file
     * @return
     */
    @Override
    public OssUploadFileDTO uploadDynamicImage(MultipartFile file) {
        return uploadImage(file, OssFileDirectory.IMAGE_DYNAMIC);
    }

    /**
     * 上传图片
     * @param file
     * @return
     */
    @Override
    public OssUploadFileDTO uploadImage(MultipartFile file, OssFileDirectory ossFileDirectory) {
        if (checkContentType(file, FileContentType.IMAGE)) {
            return contentTypeFallback(FileContentType.IMAGE);
        }
        String objectName = OssFileUtil.getObjectFullPathName(file, ossFileDirectory.getValue());
        return upload(file, objectName);
    }

    /**
     * 临时访问签名
     *
     * @param objectNameList
     * @return
     */
    @Override
    public OssTempSignDTO getTempSigns(List<String> objectNameList) {
        if (ListUtil.isEmpty(objectNameList)) {
            return OssTempSignDTO.emptyDTO();
        }
        return OssTempSignDTO.createDTO(ossUploadHelper.getTempSignUrl(objectNameList));
    }


    private OssUploadFileDTO contentTypeFallback(FileContentType fileContentType) {
        return OssUploadFileDTO.failed("文件类型不匹配，应该为：" + fileContentType.getPrefix());
    }


    /**
     * 检查文件类型
     * @param file
     * @param requireContentType
     * @return
     */
    private boolean checkContentType(MultipartFile file, FileContentType requireContentType) {
        String contentType = file.getContentType();
        if (contentType == null) {
            return true;
        }
        return !contentType.startsWith(requireContentType.getPrefix());
    }
    /**
     * 检查文件类型
     * @param file
     * @param requireContentType
     * @return
     */
    private boolean checkContentType(File file, FileContentType requireContentType) {
        String contentType;
        try {
            contentType = Files.probeContentType(file.toPath());
        } catch (IOException e) {
            log.error("probeContentType error");
            throw new RuntimeException(e);
        }
        if (contentType == null) {
            return true;
        }
        return !contentType.startsWith(requireContentType.getPrefix());
    }

    /**
     * 上传文件
     * @param file
     * @param objectName
     * @return
     */
    public OssUploadFileDTO upload(MultipartFile file, String objectName) {
        try {

            String path = ossUploadHelper.doUpload(file, objectName);
            return OssUploadFileDTO.success(path);
        } catch (UtilityException e) {
            return OssUploadFileDTO.failed(e.getMessage());
        } catch (Exception e) {
            return OssUploadFileDTO.failed("服务端异常");
        }
    }
    /**
     * 分片上传文件
     * @param file
     * @param objectName
     * @return
     */
    public OssUploadFileDTO partUpload(MultipartFile file, String objectName) {
        try {
            String path;
            path = ossUploadHelper.partUpload(file, objectName);
            return OssUploadFileDTO.success(path);
        } catch (UtilityException e) {
            return OssUploadFileDTO.failed(e.getMessage());
        } catch (Exception e) {
            return OssUploadFileDTO.failed("服务端异常");
        }
    }

}
