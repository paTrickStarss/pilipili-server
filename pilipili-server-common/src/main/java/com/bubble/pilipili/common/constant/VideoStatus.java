/*
 * Copyright (c) 2025. Bubble
 */

package com.bubble.pilipili.common.constant;

import lombok.Getter;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @author Bubble
 * @date 2025.05.13 21:15
 */
@Getter
public enum VideoStatus {

    UPLOADING(0, "上传中"),
    AUDITING(1, "审核中"),
    AUDIT_PASSED(2, "审核通过"),
    AUDIT_FAILED(3, "审核不通过"),
    BLOCK(4, "下架"),
    ;

    private final Integer value;
    private final String description;

    VideoStatus(Integer value, String description) {
        this.value = value;
        this.description = description;
    }

    /**
     * 用户个人空间视频状态列表（不可变，包含上传中、审核中、审核通过三个状态）
     */
    public static final List<VideoStatus> USER_VIDEO_STATUS_LIST =
            Collections.unmodifiableList(Arrays.asList(
                    UPLOADING,
                    AUDITING,
                    AUDIT_PASSED
            ));
}
