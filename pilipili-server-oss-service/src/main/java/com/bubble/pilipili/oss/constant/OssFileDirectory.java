/*
 * Copyright (c) 2025. Bubble
 */

package com.bubble.pilipili.oss.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * OSS存储目录枚举
 * @author Bubble
 * @date 2025.03.21 14:30
 */
@Getter
@AllArgsConstructor
public enum OssFileDirectory {

    /**
     * 视频
     */
    VIDEO_CONTENT("video/main"),
    /**
     * 动态视频
     */
    VIDEO_DYNAMIC("video/dynamic"),

    /**
     * 图片-视频封面
     */
    IMAGE_VIDEO_COVER("image/cover/video"),
    /**
     * 图片-头像
     */
    IMAGE_AVATAR("image/avatar"),
    /**
     * 图片-动态
     */
    IMAGE_DYNAMIC("image/dynamic"),
    /**
     * 图片-收藏夹封面
     */
    IMAGE_COLLECTION_COVER("image/cover/collection"),
    ;

    private final String value;
}
