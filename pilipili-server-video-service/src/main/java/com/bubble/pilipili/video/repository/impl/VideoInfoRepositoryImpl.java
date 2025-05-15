/*
 * Copyright (c) 2025. Bubble
 */

package com.bubble.pilipili.video.repository.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bubble.pilipili.common.constant.VideoStatus;
import com.bubble.pilipili.common.util.ListUtil;
import com.bubble.pilipili.common.util.StringUtil;
import com.bubble.pilipili.video.mapper.VideoInfoMapper;
import com.bubble.pilipili.video.pojo.entity.VideoInfo;
import com.bubble.pilipili.video.pojo.param.QueryVideoInfoParam;
import com.bubble.pilipili.video.repository.VideoInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

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
     * @param vidList
     * @return
     */
    @Override
    public List<VideoInfo> getVideoInfoById(List<Integer> vidList) {
        if (ListUtil.isEmpty(vidList)) {
            return Collections.emptyList();
        }
        return videoInfoMapper.selectList(
                new LambdaQueryWrapper<VideoInfo>()
                        .in(VideoInfo::getVid, vidList)
        );
    }

    /**
     * 分页条件查询已过审视频
     *
     * @param param
     * @param pageNo
     * @param pageSize
     * @return
     */
    @Override
    public Page<VideoInfo> pageQueryVideoInfo(QueryVideoInfoParam param, Long pageNo, Long pageSize) {
        Page<VideoInfo> page = new Page<>(pageNo, pageSize);
        LambdaQueryWrapper<VideoInfo> qw = getQueryWrapper(param);
        // 查询已过审的视频
        qw.eq(VideoInfo::getStatus, VideoStatus.AUDIT_PASSED.getValue());
        return videoInfoMapper.selectPage(page, qw);
    }


    /**
     * 分页查询用户的所有视频（可排序）
     *
     * @param uid
     * @param pageNo
     * @param pageSize
     * @param statusList
     * @param applyOrder
     * @param isAsc
     * @param columnFuncList
     * @return
     */
    @Override
    public Page<VideoInfo> pageQueryVideoInfoByUid(
            Integer uid, Long pageNo, Long pageSize,
            List<VideoStatus> statusList, boolean applyOrder, boolean isAsc,
            List<SFunction<VideoInfo, ?>> columnFuncList
    ) {
        Page<VideoInfo> page = new Page<>(pageNo, pageSize);
        LambdaQueryWrapper<VideoInfo> qw = new LambdaQueryWrapper<>();
        qw.eq(VideoInfo::getUid, uid);
        // 视频状态过滤
        if (ListUtil.isNotEmpty(statusList)) {
            qw.in(VideoInfo::getStatus, getStatusValue(statusList));
        }
        // 排序
        if (ListUtil.isNotEmpty(columnFuncList)) {
            qw.orderBy(applyOrder, isAsc, columnFuncList);
        }
        return videoInfoMapper.selectPage(page, qw);
    }


    /**
     * 查询用户视频数量
     *
     * @param uid
     * @param statusList
     * @return
     */
    @Override
    public Long getUserVideoCount(Integer uid, List<VideoStatus> statusList) {
        LambdaQueryWrapper<VideoInfo> qw = new LambdaQueryWrapper<>();
        qw.eq(VideoInfo::getUid, uid);
        if (ListUtil.isNotEmpty(statusList)) {
            qw.in(VideoInfo::getStatus, getStatusValue(statusList));
        }
        return videoInfoMapper.selectCount(qw);
    }

    /**
     * 根据查询条件参数生成QueryWrapper
     * @param param
     * @return
     */
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


    private List<Integer> getStatusValue(List<VideoStatus> statusList) {
        return statusList
                .stream()
                .map(VideoStatus::getValue)
                .collect(Collectors.toList());
    }
}
