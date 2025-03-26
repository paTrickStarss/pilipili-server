/*
 * Copyright (c) 2025. Bubble
 */

package com.bubble.pilipili.common.component;

import com.bubble.pilipili.common.constant.RedisKey;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

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

    public String getVideoTaskMapKey(String taskId) {
        return RedisKey.VIDEO_TASK_MAP.getKey() + RedisKey.KEY_DIVIDER.getKey() + taskId;
    }
}
