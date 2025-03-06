/*
 * Copyright (c) 2025. Bubble
 */

package com.bubble.pilipili.interact.repository.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bubble.pilipili.interact.mapper.DanmakuInfoMapper;
import com.bubble.pilipili.interact.pojo.entity.DanmakuInfo;
import com.bubble.pilipili.interact.repository.DanmakuInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author Bubble
 * @date 2025.03.06 13:45
 */
@Component
public class DanmakuInfoRepositoryImpl implements DanmakuInfoRepository {

    @Autowired
    private DanmakuInfoMapper danmakuInfoMapper;

    /**
     * @param danmakuInfo
     * @return
     */
    @Override
    public Boolean saveDanmakuInfo(DanmakuInfo danmakuInfo) {
        return danmakuInfoMapper.insert(danmakuInfo) == 1;
    }

    /**
     * @param danmakuId
     * @return
     */
    @Override
    public Boolean deleteDanmakuInfo(Integer danmakuId) {
        return danmakuInfoMapper.deleteById(danmakuId) == 1;
    }

    /**
     * @param uid
     * @param pageNo
     * @param pageSize
     * @return
     */
    @Override
    public Page<DanmakuInfo> pageQueryDanmakuInfoByUid(Integer uid, Long pageNo, Long pageSize) {
        Page<DanmakuInfo> page = new Page<>(pageNo, pageSize);
        return danmakuInfoMapper.selectPage(page,
                new LambdaQueryWrapper<DanmakuInfo>()
                        .eq(DanmakuInfo::getUid, uid)
                        .orderBy(true, false, DanmakuInfo::getVid)
                        .orderBy(true, true, DanmakuInfo::getTiming)
        );
    }

    /**
     * @param vid
     * @param pageNo
     * @param pageSize
     * @return
     */
    @Override
    public Page<DanmakuInfo> pageQueryDanmakuInfoByVid(Integer vid, Long pageNo, Long pageSize) {
        Page<DanmakuInfo> page = new Page<>(pageNo, pageSize);
        return danmakuInfoMapper.selectPage(page,
                new LambdaQueryWrapper<DanmakuInfo>()
                        .eq(DanmakuInfo::getVid, vid)
                        .orderBy(true, true, DanmakuInfo::getTiming)
        );
    }

    /**
     * @param vid
     * @param uid
     * @param pageNo
     * @param pageSize
     * @return
     */
    @Override
    public Page<DanmakuInfo> pageQueryDanmakuInfoByVidAndUid(Integer vid, Integer uid, Long pageNo, Long pageSize) {
        Page<DanmakuInfo> page = new Page<>(pageNo, pageSize);
        return danmakuInfoMapper.selectPage(page,
                new LambdaQueryWrapper<DanmakuInfo>()
                        .eq(DanmakuInfo::getVid, vid)
                        .eq(DanmakuInfo::getUid, uid)
                        .orderBy(true, true, DanmakuInfo::getTiming)
        );
    }
}
