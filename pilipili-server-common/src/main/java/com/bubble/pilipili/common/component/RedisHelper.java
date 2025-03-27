/*
 * Copyright (c) 2025. Bubble
 */

package com.bubble.pilipili.common.component;

import com.bubble.pilipili.common.constant.RedisKey;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * @author Bubble
 * @date 2025.03.26 17:05
 */
@Slf4j
@Component
public class RedisHelper {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;


    /**
     * OSS临时访问链接过期提前时间（分钟）
     */
    private static final int OSS_TEMP_KEY_EXPIRE_OFFSET = 10;

    /**
     * 保存视频任务ID映射
     * @param vid
     * @param taskId
     */
    public void saveVideoTask(Integer vid, String taskId) {
        String key = getVideoTaskMapKey(taskId);
        redisTemplate.opsForValue().set(key, vid);
        log.info("Save VideoTask map: {}:{}", key, vid);
    }

    public Integer getVideoTaskVid(String taskId) {
        return (Integer) redisTemplate.opsForValue().get(getVideoTaskMapKey(taskId));
    }

    private String getVideoTaskMapKey(String taskId) {
        return RedisKey.VIDEO_TASK_MAP.getKey() + RedisKey.KEY_DIVIDER.getKey() + taskId;
    }
    private String getOssTempAccessUrlKey(String objectName) {
        return RedisKey.OSS_TEMP_MAP.getKey() + RedisKey.KEY_DIVIDER.getKey() + objectName;
    }

    /**
     * 保存OSS临时访问链接
     * @param objectName
     * @param tempAccessUrl
     * @param expireAtTimeInSeconds
     */
    public boolean saveOssTempAccessUrl(String objectName, String tempAccessUrl, long expireAtTimeInSeconds) {
        long timeout = expireAtTimeInSeconds * 1000 - System.currentTimeMillis();
        // 提前一段时间过期
        timeout -= 1000 * 60 * OSS_TEMP_KEY_EXPIRE_OFFSET;
        if (timeout < 0) {
            return false;
        }
        redisTemplate.opsForValue().set(
                getOssTempAccessUrlKey(objectName),
                tempAccessUrl,
                timeout, TimeUnit.MILLISECONDS);
        return true;
    }

    /**
     * 获取OSS临时访问链接缓存值
     * @param objectName
     * @return 不存在则为null
     */
    public String getOssTempAccessUrl(String objectName) {
        return (String) redisTemplate.opsForValue().get(getOssTempAccessUrlKey(objectName));
    }
}
