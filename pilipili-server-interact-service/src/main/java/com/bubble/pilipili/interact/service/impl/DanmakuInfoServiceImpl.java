/*
 * Copyright (c) 2025. Bubble
 */

package com.bubble.pilipili.interact.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bubble.pilipili.common.exception.ServiceOperationException;
import com.bubble.pilipili.common.pojo.PageDTO;
import com.bubble.pilipili.common.service.InteractStatsAction;
import com.bubble.pilipili.common.util.ListUtil;
import com.bubble.pilipili.feign.api.StatsFeignAPI;
import com.bubble.pilipili.feign.pojo.entity.DanmakuStats;
import com.bubble.pilipili.interact.pojo.converter.DanmakuInfoConverter;
import com.bubble.pilipili.interact.pojo.dto.QueryDanmakuInfoDTO;
import com.bubble.pilipili.interact.pojo.entity.*;
import com.bubble.pilipili.interact.pojo.req.PageQueryDanmakuInfoReq;
import com.bubble.pilipili.interact.pojo.req.SaveDanmakuInfoReq;
import com.bubble.pilipili.interact.repository.DanmakuInfoRepository;
import com.bubble.pilipili.interact.repository.UserDanmakuRepository;
import com.bubble.pilipili.interact.service.DanmakuInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * @author Bubble
 * @date 2025.03.06 14:06
 */
@Slf4j
@Service
public class DanmakuInfoServiceImpl implements DanmakuInfoService {

    @Autowired
    private DanmakuInfoRepository danmakuInfoRepository;
    @Autowired
    private UserDanmakuRepository userDanmakuRepository;
    @Autowired
    private StatsFeignAPI statsFeignAPI;

    /**
     * 保存弹幕信息
     * @param req
     * @return
     */
    @Transactional
    @Override
    public Boolean saveDanmakuInfo(SaveDanmakuInfoReq req) {
        DanmakuInfo danmakuInfo =
                DanmakuInfoConverter.getInstance().copyFieldValue(req, DanmakuInfo.class);
        Boolean b = danmakuInfoRepository.saveDanmakuInfo(danmakuInfo);

        //todo: 更新视频弹幕统计数据

        return b;
    }

    /**
     * 删除弹幕信息
     * @param danmakuId
     * @return
     */
    @Transactional
    @Override
    public Boolean deleteDanmakuInfo(Integer danmakuId) {
        return danmakuInfoRepository.deleteDanmakuInfo(danmakuId);
    }

    /**
     * 点赞弹幕
     * @param danmakuId 
     * @param uid
     * @return
     */
    @Transactional
    @Override
    public Boolean favorDanmakuInfo(Integer danmakuId, Integer uid) {
        Boolean b = updateDanmakuInteract(
                danmakuId, uid,
                ud -> ud.setFavor(1),
                stats -> stats.setFavorCount(1L)
        );
        revokeDewDanmakuInfo(danmakuId, uid);
        return b;
    }

    /**
     * 取消点赞弹幕
     * @param danmakuId 
     * @param uid
     * @return
     */
    @Transactional
    @Override
    public Boolean revokeFavorDanmakuInfo(Integer danmakuId, Integer uid) {
        return updateDanmakuInteract(
                danmakuId, uid,
                ud -> ud.setFavor(0),
                stats -> stats.setFavorCount(-1L)
        );
    }

    /**
     * 点踩弹幕
     * @param danmakuId 
     * @param uid
     * @return
     */
    @Transactional
    @Override
    public Boolean dewDanmakuInfo(Integer danmakuId, Integer uid) {
        Boolean b = updateDanmakuInteract(
                danmakuId, uid,
                ud -> ud.setDew(1),
                stats -> stats.setDewCount(1L)
        );
        revokeFavorDanmakuInfo(danmakuId, uid);
        return b;
    }

    /**
     * 取消点踩弹幕
     * @param danmakuId 
     * @param uid
     * @return
     */
    @Transactional
    @Override
    public Boolean revokeDewDanmakuInfo(Integer danmakuId, Integer uid) {
        return updateDanmakuInteract(
                danmakuId, uid,
                ud -> ud.setDew(0),
                stats -> stats.setDewCount(-1L)
        );
    }

    /**
     * 分页查询视频弹幕
     * @param req
     * @return
     */
    @Override
    public PageDTO<QueryDanmakuInfoDTO> pageQueryDanmakuInfoByVid(PageQueryDanmakuInfoReq req) {
        if (req.getVid() == null) {
            return PageDTO.emptyPageDTO();
        }
        Page<DanmakuInfo> danmakuInfoPage =
                danmakuInfoRepository.pageQueryDanmakuInfoByVid(req.getVid(), req.getPageNo(), req.getPageSize());
        List<QueryDanmakuInfoDTO> dtoList = handleDanmakuInfo(danmakuInfoPage.getRecords());

        return new PageDTO<>(
                danmakuInfoPage.getCurrent(),
                danmakuInfoPage.getSize(),
                danmakuInfoPage.getTotal(),
                dtoList
        );
    }

    /**
     * 分页查询用户弹幕
     * @param req
     * @return
     */
    @Override
    public PageDTO<QueryDanmakuInfoDTO> pageQueryDanmakuInfoByUid(PageQueryDanmakuInfoReq req) {
        if (req.getUid() == null) {
            return PageDTO.emptyPageDTO();
        }
        Page<DanmakuInfo> danmakuInfoPage =
                danmakuInfoRepository.pageQueryDanmakuInfoByUid(req.getUid(), req.getPageNo(), req.getPageSize());
        List<QueryDanmakuInfoDTO> dtoList = handleDanmakuInfo(danmakuInfoPage.getRecords());

        return new PageDTO<>(
                danmakuInfoPage.getCurrent(),
                danmakuInfoPage.getSize(),
                danmakuInfoPage.getTotal(),
                dtoList
        );
    }

    /**
     * @param req
     * @return
     */
    @Override
    public PageDTO<QueryDanmakuInfoDTO> pageQueryDanmakuInfoByVidAndUid(PageQueryDanmakuInfoReq req) {
        if (req.getVid() == null || req.getUid() == null) {
            return PageDTO.emptyPageDTO();
        }
        Page<DanmakuInfo> danmakuInfoPage =
                danmakuInfoRepository.pageQueryDanmakuInfoByVidAndUid(
                        req.getVid(), req.getUid(), req.getPageNo(), req.getPageSize());
        List<QueryDanmakuInfoDTO> dtoList = handleDanmakuInfo(danmakuInfoPage.getRecords());

        return new PageDTO<>(
                danmakuInfoPage.getCurrent(),
                danmakuInfoPage.getSize(),
                danmakuInfoPage.getTotal(),
                dtoList
        );
    }

    /**
     * 查询弹幕统计数据并装填dto
     * @param danmakuInfoList
     * @return
     */
    private List<QueryDanmakuInfoDTO> handleDanmakuInfo(List<DanmakuInfo> danmakuInfoList) {
        if (ListUtil.isEmpty(danmakuInfoList)) {
            return Collections.emptyList();
        }

        List<Integer> idList = danmakuInfoList.stream().map(DanmakuInfo::getDanmakuId).collect(Collectors.toList());
        Map<Integer, DanmakuStats> statsMap = statsFeignAPI.getDanmakuStats(idList).getData().getStatsMap();
        List<QueryDanmakuInfoDTO> dtoList =
                DanmakuInfoConverter.getInstance().copyFieldValueList(danmakuInfoList, QueryDanmakuInfoDTO.class);
        dtoList.forEach(dto -> {
            DanmakuStats stats = statsMap.get(dto.getDanmakuId());
            if (stats != null) {
                dto.setFavorCount(stats.getFavorCount());
                dto.setDewCount(stats.getDewCount());
            }
        });

        return dtoList;
    }

    /**
     * 更新用户弹幕互动关系数据
     * @param danmakuId
     * @param uid
     * @param interactConsumer
     * @param statsConsumer
     * @return
     */
    private Boolean updateDanmakuInteract(
            Integer danmakuId, Integer uid,
            Consumer<UserDanmaku> interactConsumer,
            Consumer<DanmakuStats> statsConsumer
    ) {
        try {
            return InteractStatsAction.updateInteract(
                    UserDanmaku.class,
                    danmakuId, uid,
                    UserDanmaku::setDanmakuId,
                    userDanmakuRepository,
                    interactConsumer
            );
            // todo: 推送弹幕统计数据更新信息
        } catch (Exception e) {
            log.warn(e.getMessage());
            throw new ServiceOperationException("更新弹幕互动关系数据异常");
        }
    }
}
