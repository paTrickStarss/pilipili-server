/*
 * Copyright (c) 2025. Bubble
 */

package com.bubble.pilipili.oss.util;

import com.bubble.pilipili.common.exception.UtilityException;
import com.bubble.pilipili.common.util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

/**
 * @author Bubble
 * @date 2025.03.21 14:57
 */
@Slf4j
public class OssFileUtil {

    /**
     * 生成UUID文件名（带扩展名）
     * @param file
     * @return
     */
    public static String getFileNameWithExtension(MultipartFile file) {
        if (file == null) {
            log.warn("file is null");
            return null;
        }

        String contentType = file.getContentType();
        if (StringUtil.isEmpty(contentType)) {
            log.warn("contentType is null");
            return null;
        }

        String extension = getExtension(contentType);
        String fileName = UUID.randomUUID().toString();
        return String.join(".", fileName, extension);
    }

    /**
     * 获取文件扩展名
     * @param contentType
     * @return
     */
    public static String getExtension(String contentType) {
        String extensionName;
        switch (contentType) {
            // 图片类型
            case "image/jpeg":
                extensionName = "jpg";
                break;
            case "image/png":
                extensionName = "png";
                break;
            case "image/gif":
                extensionName = "gif";
                break;
            case "image/bmp":
                extensionName = "bmp";
                break;
            case "image/webp":
                extensionName = "webp";
                break;
            case "image/avif":
                extensionName = "avif";
                break;

            // 视频类型
            case "video/mp4":
                extensionName = "mp4";
                break;
            case "video/mpeg":
                extensionName = "mpeg";
                break;
            case "video/avi":
                extensionName = "avi";
                break;
            case "video/matroska":
                extensionName = "mkv";
                break;
            case "video/ogg":
                extensionName = "ogg";
                break;
            case "video/webm":
                extensionName = "webm";
                break;
            default:
                log.warn("unknown content type {}", contentType);
                throw new UtilityException("不支持的文件类型");
        }
        return extensionName;
    }
}
