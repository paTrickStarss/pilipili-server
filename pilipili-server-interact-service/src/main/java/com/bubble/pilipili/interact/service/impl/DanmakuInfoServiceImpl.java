/*
 * Copyright (c) 2025. Bubble
 */

package com.bubble.pilipili.interact.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bubble.pilipili.common.pojo.PageDTO;
import com.bubble.pilipili.interact.pojo.converter.DanmakuInfoConverter;
import com.bubble.pilipili.interact.pojo.dto.QueryDanmakuInfoDTO;
import com.bubble.pilipili.interact.pojo.entity.DanmakuInfo;
import com.bubble.pilipili.interact.pojo.entity.UserDanmaku;
import com.bubble.pilipili.interact.pojo.req.PageQueryDanmakuInfoReq;
import com.bubble.pilipili.interact.pojo.req.SaveDanmakuInfoReq;
import com.bubble.pilipili.interact.repository.DanmakuInfoRepository;
import com.bubble.pilipili.interact.repository.UserDanmakuRepository;
import com.bubble.pilipili.interact.service.DanmakuInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author Bubble
 * @date 2025.03.06 14:06
 */
@Service
public class DanmakuInfoServiceImpl implements DanmakuInfoService {

    @Autowired
    private DanmakuInfoRepository danmakuInfoRepository;
    @Autowired
    private UserDanmakuRepository userDanmakuRepository;

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
        return danmakuInfoRepository.saveDanmakuInfo(danmakuInfo);
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
    @Override
    public Boolean favorDanmakuInfo(Integer danmakuId, Integer uid) {
        return userDanmakuRepository.saveUserDanmaku(
                generateUserDanmaku(danmakuId, uid, userDanmaku -> {
                    userDanmaku.setFavor(1);
                    userDanmaku.setDew(0);
                })
        );
    }

    /**
     * 取消点赞弹幕
     * @param danmakuId 
     * @param uid
     * @return
     */
    @Override
    public Boolean revokeFavorDanmakuInfo(Integer danmakuId, Integer uid) {
        return userDanmakuRepository.saveUserDanmaku(
                generateUserDanmaku(danmakuId, uid, userDanmaku -> userDanmaku.setFavor(0))
        );
    }

    /**
     * 点踩弹幕
     * @param danmakuId 
     * @param uid
     * @return
     */
    @Override
    public Boolean dewDanmakuInfo(Integer danmakuId, Integer uid) {
        return userDanmakuRepository.saveUserDanmaku(
                generateUserDanmaku(danmakuId, uid, userDanmaku -> {
                    userDanmaku.setDew(1);
                    userDanmaku.setFavor(0);
                })
        );
    }

    /**
     * 取消点踩弹幕
     * @param danmakuId 
     * @param uid
     * @return
     */
    @Override
    public Boolean revokeDewDanmakuInfo(Integer danmakuId, Integer uid) {
        return userDanmakuRepository.saveUserDanmaku(
                generateUserDanmaku(danmakuId, uid, userDanmaku -> userDanmaku.setDew(0))
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
        return handleDanmakuInfo(danmakuInfoPage);
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
        return handleDanmakuInfo(danmakuInfoPage);
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
        return handleDanmakuInfo(danmakuInfoPage);
    }

    /**
     * 按弹幕ID映射为Map
     * @param records
     * @return
     */
    private Map<Integer, QueryDanmakuInfoDTO> getDanmakuIdMap(List<DanmakuInfo> records) {
        return DanmakuInfoConverter.getInstance()
                .copyFieldValueList(records, QueryDanmakuInfoDTO.class)
                .stream()
                .collect(Collectors.toMap(
                        QueryDanmakuInfoDTO::getDanmakuId, Function.identity(),
                        (a, b) -> a
                ));
    }

    /**
     * 处理分页查询结果
     * @param page
     * @return
     */
    private PageDTO<QueryDanmakuInfoDTO> handleDanmakuInfo(Page<DanmakuInfo> page) {
        Map<Integer, QueryDanmakuInfoDTO> dtoMap = getDanmakuIdMap(page.getRecords());

        // 统计数据
        userDanmakuRepository.getDanmakuStats(new ArrayList<>(dtoMap.keySet()))
                .forEach(stats -> {
                    dtoMap.get(stats.getDanmakuId()).setFavorCount(stats.getFavorCount());
                    dtoMap.get(stats.getDanmakuId()).setDewCount(stats.getDewCount());
                });

        PageDTO<QueryDanmakuInfoDTO> pageDTO = new PageDTO<>();
        pageDTO.setPageNo(page.getCurrent());
        pageDTO.setPageSize(page.getSize());
        pageDTO.setTotal(page.getTotal());
        pageDTO.setData(new ArrayList<>(dtoMap.values()));

        return pageDTO;
    }

    private UserDanmaku generateUserDanmaku(
            Integer danmakuId, Integer uid,
            Consumer<UserDanmaku> consumer
    ) {
        UserDanmaku userDanmaku = new UserDanmaku();
        userDanmaku.setDanmakuId(danmakuId);
        userDanmaku.setUid(uid);
        consumer.accept(userDanmaku);
        return userDanmaku;
    }
}
