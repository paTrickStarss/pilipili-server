/*
 * Copyright (c) 2025. Bubble
 */

package com.bubble.pilipili.oss.service.impl;

import com.bubble.pilipili.common.exception.UtilityException;
import com.bubble.pilipili.feign.pojo.dto.OssUploadFileDTO;
import com.bubble.pilipili.oss.constant.FileContentType;
import com.bubble.pilipili.oss.constant.OssFileDirectory;
import com.bubble.pilipili.oss.service.OssService;
import com.bubble.pilipili.oss.util.OssUploadHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author Bubble
 * @date 2025.03.21 14:19
 */
@Slf4j
@Service
public class OssServiceImpl implements OssService {

    @Autowired
    private OssUploadHelper ossUploadHelper;


    /**
     * 上传视频
     * @param file
     * @return
     */
    @Override
    public OssUploadFileDTO uploadVideo(MultipartFile file) {
        if (checkContentType(file, FileContentType.VIDEO)) {
            return contentTypeFallback(FileContentType.VIDEO);
        }
        return upload(file, OssFileDirectory.VIDEO_CONTENT);
    }

    /**
     * 上传动态视频
     *
     * @param file
     * @return
     */
    @Override
    public OssUploadFileDTO uploadDynamicVideo(MultipartFile file) {
        if (checkContentType(file, FileContentType.VIDEO)) {
            return contentTypeFallback(FileContentType.VIDEO);
        }
        return upload(file, OssFileDirectory.VIDEO_DYNAMIC);
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
        return upload(file, ossFileDirectory);
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
     * 上传文件
     * @param file
     * @param ossFileDirectory
     * @return
     */
    public OssUploadFileDTO upload(MultipartFile file, OssFileDirectory ossFileDirectory) {
        try {

            String path = ossUploadHelper.doUpload(file, ossFileDirectory.getValue());
            return OssUploadFileDTO.success(path);
        } catch (UtilityException e) {
            return OssUploadFileDTO.failed(e.getMessage());
        } catch (Exception e) {
            return OssUploadFileDTO.failed("服务端异常");
        }
    }
}
