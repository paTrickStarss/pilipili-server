/*
 * Copyright (c) 2025. Bubble
 */

package com.bubble.pilipili.video.repository.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bubble.pilipili.common.util.StringUtil;
import com.bubble.pilipili.video.mapper.VideoInfoMapper;
import com.bubble.pilipili.video.pojo.entity.VideoInfo;
import com.bubble.pilipili.video.pojo.param.QueryVideoInfoParam;
import com.bubble.pilipili.video.repository.VideoInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 视频信息数据库操作层
 * @author Bubble
 * @date 2025/01/21 14:58
 */
@Component
public class VideoInfoRepositoryImpl implements VideoInfoRepository {

    @Autowired
    private VideoInfoMapper videoInfoMapper;

    /**
     * @param videoInfo
     * @return
     */
    @Override
    public Boolean saveVideoInfo(VideoInfo videoInfo) {
        int insert = videoInfoMapper.insert(videoInfo);
        return insert > 0;
    }

    /**
     * @param videoInfo
     * @return
     */
    @Override
    public Boolean updateVideoInfo(VideoInfo videoInfo) {
        int update = videoInfoMapper.updateById(videoInfo);
        return update > 0;
    }

    /**
     * @param vid
     * @return
     */
    @Override
    public Boolean deleteVideoInfo(Integer vid) {
        int row = videoInfoMapper.deleteById(vid);
        return row > 0;
    }

    /**
     * @param vid
     * @return
     */
    @Override
    public VideoInfo getVideoInfoById(Integer vid) {
        return videoInfoMapper.selectById(vid);
    }

    /**
     * @param uid
     * @param pageNo
     * @param pageSize
     * @return
     */
    @Override
    public Page<VideoInfo> pageQueryVideoInfoByUid(Integer uid, Long pageNo, Long pageSize) {
        Page<VideoInfo> page = new Page<>(pageNo, pageSize);
        return videoInfoMapper.selectPage(page, new LambdaQueryWrapper<VideoInfo>().eq(VideoInfo::getUid, uid));
    }

    /**
     * @param param
     * @param pageNo
     * @param pageSize
     * @return
     */
    @Override
    public Page<VideoInfo> pageQueryVideoInfo(QueryVideoInfoParam param, Long pageNo, Long pageSize) {
        Page<VideoInfo> page = new Page<>(pageNo, pageSize);
        LambdaQueryWrapper<VideoInfo> queryWrapper = getQueryWrapper(param);
        return videoInfoMapper.selectPage(page, queryWrapper);
    }

    private LambdaQueryWrapper<VideoInfo> getQueryWrapper(QueryVideoInfoParam param) {
        LambdaQueryWrapper<VideoInfo> queryWrapper = new LambdaQueryWrapper<>();

        if (StringUtil.isNotEmpty(param.getTitle())) {
            queryWrapper.like(VideoInfo::getTitle, param.getTitle());
        }
        if (StringUtil.isNotEmpty(param.getTag())) {
            queryWrapper.like(VideoInfo::getTag, param.getTag());
        }
        if (StringUtil.isNotEmpty(param.getPublishDateStart())) {
            queryWrapper.ge(VideoInfo::getPublishTime, param.getPublishDateStart());
        }
        if (StringUtil.isNotEmpty(param.getPublishDateEnd())) {
            queryWrapper.le(VideoInfo::getPublishTime, param.getPublishDateEnd());
        }

        return queryWrapper;
    }
}
