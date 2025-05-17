/*
 * Copyright (c) 2025. Bubble
 */

package com.bubble.pilipili.user.util;

import com.bubble.pilipili.common.component.RedisHelper;
import com.bubble.pilipili.common.constant.RedisKey;
import org.springframework.stereotype.Component;

/**
 * @author Bubble
 * @date 2025.05.17 21:04
 */
@Component
public class UserRedisHelper extends RedisHelper {

    private String getCacheKey(Integer uid) {
        return getCacheKey(RedisKey.USER_INFO, uid);
    }
}
