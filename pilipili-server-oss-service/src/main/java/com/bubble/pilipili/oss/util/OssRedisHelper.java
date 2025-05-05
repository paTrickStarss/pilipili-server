/*
 * Copyright (c) 2025. Bubble
 */

package com.bubble.pilipili.oss.util;

import com.bubble.pilipili.common.component.RedisHelper;
import com.bubble.pilipili.common.constant.RedisKey;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * Oss服务Redis操作类
 * @author Bubble
 * @date 2025.04.14 14:52
 */
@Component
public class OssRedisHelper extends RedisHelper {

//    @Autowired
//    private RedisTemplate<String, Object> redisTemplate;

    /**
     * OSS临时访问链接过期提前时间（分钟）
     */
    private static final int OSS_TEMP_KEY_EXPIRE_OFFSET = 10;

    /**
     * 保存OSS临时访问链接
     * @param objectName
     * @param tempAccessUrl
     * @param expireAtTimeInSeconds
     * @return true - 保存成功，false - 临近过期时间，无法保存
     */
    public void saveOssTempAccessUrl(String objectName, String tempAccessUrl, long expireAtTimeInSeconds) {
        long timeout = expireAtTimeInSeconds * 1000 - System.currentTimeMillis();
        // 提前一段时间过期
        timeout -= 1000 * 60 * OSS_TEMP_KEY_EXPIRE_OFFSET;
        if (timeout < 0) {
            return;
        }
        redisTemplate.opsForValue().set(
                getOssTempAccessUrlKey(objectName),
                tempAccessUrl,
                timeout, TimeUnit.MILLISECONDS);
    }

    /**
     * 获取OSS临时访问链接缓存值
     * @param objectName
     * @return 不存在则为null
     */
    public String getOssTempAccessUrl(String objectName) {
        return (String) redisTemplate.opsForValue().get(getOssTempAccessUrlKey(objectName));
    }


    private String getOssTempAccessUrlKey(String objectName) {
        return RedisKey.OSS_TEMP_MAP.getKey() + RedisKey.KEY_DIVIDER.getKey() + objectName;
    }
}
