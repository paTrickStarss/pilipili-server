/*
 * Copyright (c) 2025. Bubble
 */

package com.bubble.pilipili.oss.service;

import com.bubble.pilipili.feign.pojo.dto.OssUploadFileDTO;
import com.bubble.pilipili.oss.constant.OssFileDirectory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author Bubble
 * @date 2025.03.21 16:08
 */
@Service
public interface OssService {

    /**
     * 上传视频
     * @param file
     * @return
     */
    OssUploadFileDTO uploadVideo(MultipartFile file);

    /**
     * 上传动态视频
     * @param file
     * @return
     */
    OssUploadFileDTO uploadDynamicVideo(MultipartFile file);

    /**
     * 上传视频封面
     * @param file
     * @return
     */
    OssUploadFileDTO uploadVideoCover(MultipartFile file);
    /**
     * 上传头像
     * @param file
     * @return
     */
    OssUploadFileDTO uploadAvatar(MultipartFile file);
    /**
     * 上传头像
     * @param file
     * @return
     */
    OssUploadFileDTO uploadCollectionCover(MultipartFile file);
    /**
     * 上传头像
     * @param file
     * @return
     */
    OssUploadFileDTO uploadDynamicImage(MultipartFile file);

    /**
     * 上传图片
     * @param file
     * @return
     */
    OssUploadFileDTO uploadImage(MultipartFile file, OssFileDirectory ossFileDirectory);

}
