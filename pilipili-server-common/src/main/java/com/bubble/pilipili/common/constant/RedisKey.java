/*
 * Copyright (c) 2024-2025. Bubble
 */

package com.bubble.pilipili.common.constant;

import lombok.Getter;

/**
 * @author liweixin@hcrc1.wecom.work
 * @date 2024/10/24
 */
@Getter
public enum RedisKey {

    RESOURCE_ROLES_MAP("RESOURCE_ROLES_MAP"),
    USER_TOKEN_MAP("USER_TOKEN_MAP"),
    VIDEO_TASK_MAP("VIDEO_TASK_MAP"),
    OSS_TEMP_MAP("OSS_TEMP_MAP"),
    CATEGORY_LIST("CATEGORY_LIST"),
    VIDEO_INFO("VIDEO_INFO"),
    VIDEO_STATS("VIDEO_STATS"),
    USER_VIDEO("USER_VIDEO"),
    USER_VIDEO_COUNT("USER_VIDEO_COUNT"),
    USER_VIDEO_ID_LIST("USER_VIDEO_ID_LIST"),
    COMMENT_STATS("COMMENT_STATS"),
    DANMAKU_STATS("DANMAKU_STATS"),
    DYNAMIC_STATS("DANMAKU_STATS"),
    USER_STATS("USER_STATS"),
    USER_INFO("USER_INFO"),
    KEY_DIVIDER(":"),
    ;

    private final String key;

    RedisKey(String key) {
        this.key = key;
    }

}
