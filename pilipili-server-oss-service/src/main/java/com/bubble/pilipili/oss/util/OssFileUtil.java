/*
 * Copyright (c) 2025. Bubble
 */

package com.bubble.pilipili.oss.util;

import com.bubble.pilipili.common.exception.UtilityException;
import com.bubble.pilipili.common.util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.UUID;

/**
 * @author Bubble
 * @date 2025.03.21 14:57
 */
@Slf4j
public class OssFileUtil {

    public static final String TMP_DIR = "T:\\tmp\\oss\\";

    public static String getObjectFullPathName(MultipartFile file, String path) {
        String fileName = OssFileUtil.getFileNameWithExtension(file);
        String objFullPathName = fileName;
        if (StringUtil.isNotEmpty(path)) {
            objFullPathName = String.join("/", path, fileName);
        }
        return objFullPathName;
    }

    /**
     * 生成Hls视频主播放列表OSS访问对象名<br>
     * eg: /video/main/UUID...../master.m3u8
     * @return
     */
    public static String generateHlsObjFullPathName(String path) {
        String hlsFileName = String.join("/", UUID.randomUUID().toString(), FFmpegHelper.HLS_MASTER_PL_NAME);
        String objFullPathName = hlsFileName;
        if (StringUtil.isNotEmpty(path)) {
            objFullPathName = String.join("/", path, hlsFileName);
        }
        return objFullPathName;
    }

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

    /**
     * 去除文件名包含的扩展名
     * @param fileName
     * @return
     */
    public static String getFileNameWithoutExtension(String fileName) {
        return getFileNameWithoutExtension(fileName, "");
    }
    /**
     * 去除文件名包含的扩展名
     * @param fileName
     * @return
     */
    public static String getFileNameWithoutExtension(String fileName, String postfix) {
        int i = fileName.lastIndexOf('.');
        if (i > 0) {
            return fileName.substring(0, i);
        }
        if (postfix == null) {
            return fileName;
        }
        return fileName + postfix;
    }

    /**
     * 删除目录及其所有子文件
     * @param directory
     * @throws IOException
     */
    public static void deleteDirectory(Path directory) {
        try {
            Files.walkFileTree(directory, new SimpleFileVisitor<Path>() {
                @NotNull
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    Files.delete(file);
                    return FileVisitResult.CONTINUE;
                }

                @NotNull
                @Override
                public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                    Files.delete(dir);
                    return FileVisitResult.CONTINUE;
                }
            });
        } catch (IOException e) {
            log.error("目录[{}]删除失败: {}", directory, e.getMessage());
            throw new RuntimeException(e);
        }
    }

}
