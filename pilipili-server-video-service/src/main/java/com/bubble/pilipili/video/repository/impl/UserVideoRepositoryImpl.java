/*
 * Copyright (c) 2025. Bubble
 */

package com.bubble.pilipili.video.repository.impl;

import com.bubble.pilipili.common.repository.impl.CommonRepoImpl;
import com.bubble.pilipili.video.mapper.UserVideoMapper;
import com.bubble.pilipili.video.pojo.entity.UserVideo;
import com.bubble.pilipili.video.repository.UserVideoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author Bubble
 * @date 2025.03.07 21:45
 */
@Component
public class UserVideoRepositoryImpl implements UserVideoRepository {

    @Autowired
    private UserVideoMapper userVideoMapper;

    /**
     * 保存互动数据
     *
     * @param interactEntity
     * @return
     */
    @Override
    public Boolean saveInteract(UserVideo interactEntity) {
        return CommonRepoImpl.save(
            interactEntity,
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
}
