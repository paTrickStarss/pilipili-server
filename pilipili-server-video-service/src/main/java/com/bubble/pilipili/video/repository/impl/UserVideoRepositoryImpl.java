/*
 * Copyright (c) 2025. Bubble
 */

package com.bubble.pilipili.video.repository.impl;

import com.bubble.pilipili.common.repository.impl.CommonRepoImpl;
import com.bubble.pilipili.video.mapper.UserVideoMapper;
import com.bubble.pilipili.video.pojo.dto.QueryVideoStatsDTO;
import com.bubble.pilipili.video.pojo.entity.UserVideo;
import com.bubble.pilipili.video.repository.UserVideoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

/**
 * @author Bubble
 * @date 2025.03.07 21:45
 */
@Component
public class UserVideoRepositoryImpl implements UserVideoRepository {

    @Autowired
    private UserVideoMapper userVideoMapper;

    /**
     * 保存用户视频互动信息
     * @param userVideo
     * @return
     */
    @Override
    public Boolean saveUserVideo(UserVideo userVideo) {
        return CommonRepoImpl.save(
                userVideo,
                UserVideo::getVid,
                UserVideo::getUid,
                (luw, uv) -> {
                    if (uv.getFavor() != null) {
                        luw.set(UserVideo::getFavor, uv.getFavor());
                    }
                    if (uv.getCoin() != null) {
                        luw.set(UserVideo::getCoin, uv.getCoin());
                    }
                    if (uv.getCollect() != null) {
                        luw.set(UserVideo::getCollect, uv.getCollect());
                    }
                    if (uv.getRepost() != null) {
                        luw.set(UserVideo::getRepost, uv.getRepost());
                    }
                    if (uv.getDew() != null) {
                        luw.set(UserVideo::getDew, uv.getDew());
                    }
                    if (uv.getLastWatchTime() != null) {
                        luw.set(UserVideo::getLastWatchTime, uv.getLastWatchTime());
                    }
                },
                userVideoMapper
        );
    }

    /**
     * @param vid
     * @return
     */
    @Override
    public QueryVideoStatsDTO countVideoStats(Integer vid) {
        return countVideoStats(Collections.singletonList(vid)).get(0);
    }

    /**
     * 批量获取视频统计数据（SQL执行聚合统计）
     * @param vidList
     * @return
     */
    @Override
    public List<QueryVideoStatsDTO> countVideoStats(List<Integer> vidList) {
        return CommonRepoImpl.getStatsBatch(
                vidList,
                QueryVideoStatsDTO.class,
                (uv, dto) -> {
                    dto.setVid(uv.getVid());
                    dto.setFavorCount(Long.valueOf(uv.getFavor()));
                    dto.setCoinCount(Long.valueOf(uv.getCoin()));
                    dto.setCollectCount(Long.valueOf(uv.getCollect()));
                    dto.setRepostCount(Long.valueOf(uv.getRepost()));
                    dto.setDewCount(Long.valueOf(uv.getDew()));
                },
                userVideoMapper,
                "vid",
                "favor", "coin", "collect", "repost", "dew"
        );
    }

}
