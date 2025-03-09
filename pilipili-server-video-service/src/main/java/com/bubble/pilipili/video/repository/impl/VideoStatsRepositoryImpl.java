/*
 * Copyright (c) 2025. Bubble
 */

package com.bubble.pilipili.video.repository.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.bubble.pilipili.video.mapper.VideoStatsMapper;
import com.bubble.pilipili.video.pojo.entity.VideoStats;
import com.bubble.pilipili.video.repository.VideoStatsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author Bubble
 * @date 2025.03.09 14:43
 */
@Component
public class VideoStatsRepositoryImpl implements VideoStatsRepository {

    @Autowired
    private VideoStatsMapper videoStatsMapper;

    /**
     * 保存视频统计数据
     * @param videoStats
     * @return
     */
    @Override
    public Boolean saveVideoStats(VideoStats videoStats) {
        return videoStatsMapper.insertOrUpdate(videoStats);
    }

    /**
     * 批量获取视频统计数据（直接从表中查询）
     * @param vidList
     * @return
     */
    @Override
    public Map<Integer, VideoStats> getVideoStats(List<Integer> vidList) {
        return videoStatsMapper.selectList(
                new LambdaQueryWrapper<VideoStats>()
                        .in(VideoStats::getVid, vidList)
        ).stream().collect(Collectors.toMap(VideoStats::getVid, Function.identity()));
    }

}
