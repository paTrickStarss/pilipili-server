/*
 * Copyright (c) 2025. Bubble
 */

package com.bubble.pilipili.video.repository;

import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bubble.pilipili.video.pojo.entity.VideoInfo;
import com.bubble.pilipili.video.pojo.param.QueryVideoInfoParam;

import java.util.List;

/**
 * @author Bubble
 * @date 2025/01/20 18:41
 */
public interface VideoInfoRepository {

    Boolean saveVideoInfo(VideoInfo videoInfo);
    Boolean updateVideoInfo(VideoInfo videoInfo);
    Boolean deleteVideoInfo(Integer vid);
    VideoInfo getVideoInfoById(Integer vid);
    List<VideoInfo> getVideoInfoById(List<Integer> vidList);
    Page<VideoInfo> pageQueryVideoInfoByUid(Integer uid, Long pageNo, Long pageSize);

    /**
     * 分页查询用户的所有视频（可排序）
     * @param uid
     * @param pageNo
     * @param pageSize
     * @param applyOrder
     * @param isAsc
     * @param columnFuncList
     * @return
     */
    Page<VideoInfo> pageQueryVideoInfoByUid(
            Integer uid, Long pageNo, Long pageSize,
            boolean applyOrder, boolean isAsc, List<SFunction<VideoInfo, ?>> columnFuncList);
    /**
     * 分页查询用户的所有已上架视频（可排序）
     * @param uid
     * @param pageNo
     * @param pageSize
     * @param applyOrder
     * @param isAsc
     * @param columnFuncList
     * @return
     */
    Page<VideoInfo> pageQueryPassedVideoInfoByUid(
            Integer uid, Long pageNo, Long pageSize,
            boolean applyOrder, boolean isAsc, List<SFunction<VideoInfo, ?>> columnFuncList);
    Page<VideoInfo> pageQueryVideoInfo(QueryVideoInfoParam param, Long pageNo, Long pageSize);
}
