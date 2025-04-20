/*
 * Copyright (c) 2025. Bubble
 */

package com.bubble.pilipili.video.util;

import com.bubble.pilipili.common.constant.RedisKey;
import com.bubble.pilipili.video.pojo.dto.QueryCategoryDTO;
import com.bubble.pilipili.video.pojo.entity.VideoInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * @author Bubble
 * @date 2025.04.14 14:57
 */
@Component
public class VideoRedisHelper {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    private static final Random RANDOM = new Random();
    /**
     * 缓存过期基础时间：3天，为了防止缓存集中过期，设置过期时间时应取浮动值
     */
    public static final long EXPIRE_TIME = 60 * 60 * 24 * 3;
    /**
     * 缓存过期时间浮动范围：上下2小时
     */
    public static final long EXPIRE_TIME_VARIANT = 60 * 60 * 2;

    /**
     * 缓存视频分区列表
     * @param categoryList
     */
    public void saveCategoryList(List<QueryCategoryDTO> categoryList) {
        saveCache(RedisKey.CATEGORY_LIST.getKey(), categoryList, EXPIRE_TIME);
    }

    /**
     * 获取视频分区列表缓存值
     * @return
     */
    public List<QueryCategoryDTO> getCategoryList() {
        return getCache(RedisKey.CATEGORY_LIST.getKey());
    }

    /**
     * 缓存视频信息
     * @param videoInfo
     */
    public void saveVideoInfo(VideoInfo videoInfo) {
        saveCache(
                concatKey(RedisKey.VIDEO_INFO.getKey(), videoInfo.getVid()),
                videoInfo
        );
    }
    /**
     * 获取视频信息缓存
     * @param vid
     * @return
     */
    public VideoInfo getVideoInfo(Integer vid) {
        return getCache(concatKey(RedisKey.VIDEO_INFO.getKey(), vid));
    }

    /**
     * 拼接key
     * @param root
     * @param ids
     * @return
     */
    private String concatKey(String root, Integer... ids) {
        StringBuilder sb = new StringBuilder();
        sb.append(root);
        for (int id : ids) {
            sb.append(RedisKey.KEY_DIVIDER.getKey()).append(id);
        }
        return sb.toString();
    }

    public <V> void saveCache(String key, V value) {
        long timeout = ((long) (EXPIRE_TIME + ((RANDOM.nextBoolean()? 1:-1) * Math.random() * EXPIRE_TIME_VARIANT)));
        saveCache(key, value, timeout);
    }
    public <V> void saveCache(String key, V value, long timeout) {
        redisTemplate.opsForValue().set(
                key, value,
                timeout,
                TimeUnit.SECONDS
        );
    }

    public <V> V getCache(String key) {
        return (V) redisTemplate.opsForValue().get(key);
    }
}
