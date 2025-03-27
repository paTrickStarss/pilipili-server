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
    KEY_DIVIDER(":"),
    ;

    private final String key;

    RedisKey(String key) {
        this.key = key;
    }

}
