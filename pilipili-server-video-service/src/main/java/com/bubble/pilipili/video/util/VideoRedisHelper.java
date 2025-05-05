/*
 * Copyright (c) 2025. Bubble
 */

package com.bubble.pilipili.video.util;

import com.bubble.pilipili.common.component.RedisHelper;
import com.bubble.pilipili.common.constant.RedisKey;
import com.bubble.pilipili.video.pojo.dto.QueryCategoryDTO;
import com.bubble.pilipili.video.pojo.entity.VideoInfo;
import org.redisson.spring.cache.NullValue;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

/**
 * @author Bubble
 * @date 2025.04.14 14:57
 */
@Component
public class VideoRedisHelper extends RedisHelper {

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
        Object cache = getCache(RedisKey.CATEGORY_LIST.getKey());
        if (cache == null) {
            return null;
        }
        return (List<QueryCategoryDTO>) cache;
    }

    public void saveVideoInfo(Integer vid, VideoInfo videoInfo) {
        saveCache(concatKey(RedisKey.VIDEO_INFO.getKey(), vid), videoInfo);
    }

    public VideoInfo getVideoInfo(Integer vid) {
        Object cache = getCache(concatKey(RedisKey.VIDEO_INFO.getKey(), vid));
        if (cache == null || cache instanceof NullValue) {
            return null;
        }
        return (VideoInfo) cache;
    }

    /**
     * 缓存用户视频id列表
     * @param uid
     * @param vidList
     */
    public void saveUserVideoIdList(Integer uid, List<Integer> vidList) {
        saveCache(concatKey(RedisKey.USER_VIDEO_ID_LIST.getKey(), uid), vidList);
    }

    /**
     * 获取用户视频id列表缓存
     * @param uid
     * @param pageNo
     * @param pageSize
     * @return
     */
    public List<Integer> getUserVideoIdList(Integer uid, Integer pageNo, Integer pageSize) {
        Object cache = getCache(concatKey(RedisKey.USER_VIDEO_ID_LIST.getKey(), uid, pageNo, pageSize));
        if (cache == null || cache instanceof NullValue) {
            return Collections.emptyList();
        }
        return (List<Integer>) cache;
    }

    /**
     * 缓存用户视频数量
     * @param uid
     * @param count
     */
    public void saveUserVideoCount(Integer uid, Long count) {
        saveCache(
                concatKey(RedisKey.USER_VIDEO_COUNT.getKey(), uid),
                count
        );
    }

    public Long getUserVideoCount(Integer uid) {
        Object cache = getCache(concatKey(RedisKey.USER_VIDEO_COUNT.getKey(), uid));
        if (cache == null || cache instanceof NullValue) {
            return null;
        }
        return (Long) cache;
    }

}
