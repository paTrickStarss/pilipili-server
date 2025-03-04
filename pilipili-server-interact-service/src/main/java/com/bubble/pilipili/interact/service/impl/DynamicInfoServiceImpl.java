/*
 * Copyright (c) 2025. Bubble
 */

package com.bubble.pilipili.interact.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bubble.pilipili.common.exception.RepositoryException;
import com.bubble.pilipili.common.pojo.PageDTO;
import com.bubble.pilipili.interact.pojo.converter.DynamicAttachConverter;
import com.bubble.pilipili.interact.pojo.converter.DynamicInfoConverter;
import com.bubble.pilipili.interact.pojo.dto.QueryDynamicAttachDTO;
import com.bubble.pilipili.interact.pojo.dto.QueryDynamicInfoDTO;
import com.bubble.pilipili.interact.pojo.dto.QueryDynamicStatsDTO;
import com.bubble.pilipili.interact.pojo.entity.DynamicAttach;
import com.bubble.pilipili.interact.pojo.entity.DynamicInfo;
import com.bubble.pilipili.interact.pojo.entity.UserDynamic;
import com.bubble.pilipili.interact.pojo.req.PageQueryDynamicInfoReq;
import com.bubble.pilipili.interact.pojo.req.SaveDynamicAttachReq;
import com.bubble.pilipili.interact.pojo.req.SaveDynamicInfoReq;
import com.bubble.pilipili.interact.repository.DynamicAttachRepository;
import com.bubble.pilipili.interact.repository.DynamicInfoRepository;
import com.bubble.pilipili.interact.repository.UserDynamicRepository;
import com.bubble.pilipili.interact.service.DynamicInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author Bubble
 * @date 2025.03.01 16:37
 */
@Slf4j
@Service
public class DynamicInfoServiceImpl implements DynamicInfoService {

    @Autowired
    private DynamicInfoRepository dynamicInfoRepository;
    @Autowired
    private DynamicAttachRepository dynamicAttachRepository;
    @Autowired
    private UserDynamicRepository userDynamicRepository;

    /**
     * 保存动态信息
     * @param saveDynamicInfoReq
     * @return
     */
    @Transactional
    @Override
    public Boolean saveDynamicInfo(SaveDynamicInfoReq saveDynamicInfoReq) {
        DynamicInfo dynamicInfo =
                DynamicInfoConverter.getInstance()
                        .copyFieldValue(saveDynamicInfoReq, DynamicInfo.class);
        try {
            Boolean saveInfo = dynamicInfoRepository.saveDynamicInfo(dynamicInfo);
            if (!saveInfo) {
                throw new RepositoryException("保存动态主表信息失败");
            }

            List<SaveDynamicAttachReq> attachList = saveDynamicInfoReq.getAttachList();
            List<DynamicAttach> dynamicAttachList =
                    attachList.stream()
                            .map(attach -> {
                                attach.setDid(dynamicInfo.getDid());
                                return DynamicAttachConverter.getInstance()
                                        .copyFieldValue(attach, DynamicAttach.class);
                            })
                            .collect(Collectors.toList());
            Boolean saveAttach = dynamicAttachRepository.saveDynamicAttachBatch(dynamicAttachList);
            if (!saveAttach) {
                throw new RepositoryException("保存动态附件信息失败");
            }

        } catch (Exception e) {
            log.error("保存动态信息失败:\n{}\n{}", saveDynamicInfoReq, e.getMessage());
            return false;
        }
        return true;
    }

    /**
     * 更新动态信息
     * @param saveDynamicInfoReq
     * @return
     */
    @Transactional
    @Override
    public Boolean updateDynamicInfo(SaveDynamicInfoReq saveDynamicInfoReq) {
        DynamicInfo dynamicInfo =
                DynamicInfoConverter.getInstance().copyFieldValue(saveDynamicInfoReq, DynamicInfo.class);
        try {
            Boolean saveMain = dynamicInfoRepository.updateDynamicInfo(dynamicInfo);
            if (!saveMain) {
                throw new RepositoryException("更新动态主表信息失败");
            }

            List<SaveDynamicAttachReq> attachRemoveList = saveDynamicInfoReq.getAttachRemoveList();
            if (attachRemoveList != null && !attachRemoveList.isEmpty()) {
                List<String> removeUUIDList = attachRemoveList.stream()
                        .map(SaveDynamicAttachReq::getAttachUUID)
                        .collect(Collectors.toList());
                Boolean removeAttach = dynamicAttachRepository.deleteDynamicAttachByUUIDBatch(removeUUIDList);
                if (!removeAttach) {
                    throw new RepositoryException("删除动态附件信息失败");
                }
            }

            List<SaveDynamicAttachReq> attachList = saveDynamicInfoReq.getAttachList();
            if (attachList != null && !attachList.isEmpty()) {
                List<DynamicAttach> dynamicAttachList = attachList.stream()
                        .map(attach -> {
                            DynamicAttach dynamicAttach = DynamicAttachConverter.getInstance()
                                            .copyFieldValue(attach, DynamicAttach.class);
                            dynamicAttach.setDid(dynamicInfo.getDid());
                            return dynamicAttach;
                        })
                        .collect(Collectors.toList());
                Boolean saveAttach = dynamicAttachRepository.saveDynamicAttachBatch(dynamicAttachList);
                if (!saveAttach) {
                    throw new RepositoryException("保存动态附件信息失败");
                }
            }
        } catch (Exception e) {
            log.error("更新动态信息失败:\n{}\n{}", e.getMessage(), saveDynamicInfoReq);
            return false;
        }
        return true;
    }

    /**
     * 删除动态信息
     * @param did
     * @return
     */
    @Override
    public Boolean deleteDynamicInfo(Integer did) {
        Boolean b = dynamicAttachRepository.deleteDynamicAttachByDid(did);
        return dynamicInfoRepository.deleteDynamicInfoByDid(did);
    }


    /**
     *
     * @param did
     * @param uid
     * @param favor
     * @return
     */
    @Override
    public Boolean favorDynamicInfo(Integer did, Integer uid, Integer favor) {
        UserDynamic userDynamic = new UserDynamic();
        userDynamic.setDid(did);
        userDynamic.setUid(uid);
        userDynamic.setFavor(favor);
        return userDynamicRepository.saveUserDynamic(userDynamic);
    }

    /**
     * @param did
     * @param uid
     * @return
     */
    @Override
    public Boolean repostDynamicInfo(Integer did, Integer uid) {
        UserDynamic userDynamic = new UserDynamic();
        userDynamic.setDid(did);
        userDynamic.setUid(uid);
        userDynamic.setRepost(1);
        return userDynamicRepository.saveUserDynamic(userDynamic);
    }

    /**
     * 查询指定动态信息
     * @param did
     * @return
     */
    @Override
    public QueryDynamicInfoDTO queryDynamicInfoDTO(Integer did) {
//        查询动态信息
        DynamicInfo dynamicInfo = dynamicInfoRepository.queryDynamicInfoByDid(did);
        if (dynamicInfo == null) {
            return null;
        }
//        查询附件信息
        List<DynamicAttach> attachList = dynamicAttachRepository.listDynamicAttachByDid(did);
//        查询统计数据
        QueryDynamicStatsDTO dynamicStats = userDynamicRepository.getDynamicStats(did);

        QueryDynamicInfoDTO dto =
                DynamicInfoConverter.getInstance().copyFieldValue(dynamicInfo, QueryDynamicInfoDTO.class);
        dto.setAttachList(
                DynamicAttachConverter.getInstance().copyFieldValueList(attachList, QueryDynamicAttachDTO.class)
        );
        dto.setFavorCount(dynamicStats.getFavorCount());
        dto.setRepostCount(dynamicStats.getRepostCount());

        return dto;
    }

    /**
     * 分页查询用户动态信息
     * @param req
     * @return
     */
    @Override
    public PageDTO<QueryDynamicInfoDTO> pageQueryDynamicInfoByUid(PageQueryDynamicInfoReq req) {
        Page<DynamicInfo> dynamicInfoPage =
                dynamicInfoRepository.pageQueryDynamicInfoByUid(req.getUid(), req.getPageNo(), req.getPageSize());

        // 按did映射
        Map<Integer, QueryDynamicInfoDTO> dynamicInfoDTOMap =
                DynamicInfoConverter.getInstance().copyFieldValueList(dynamicInfoPage.getRecords(), QueryDynamicInfoDTO.class)
                        .stream()
                        .collect(Collectors.toMap(
                            QueryDynamicInfoDTO::getDid, Function.identity(),
                            (firstValue, lastValue) -> firstValue)  // did是唯一主键，是不会重复出现的，这里只是了解一下
                        );

        // 批量查询attachList
        List<Integer> didList = new ArrayList<>(dynamicInfoDTOMap.keySet()); //infoList.stream().map(DynamicInfo::getDid).collect(Collectors.toList());
        Map<Integer, List<DynamicAttach>> attachGrouped =
                dynamicAttachRepository.listDynamicAttachByDidBatch(didList);
        // 绑定attachList
        attachGrouped.forEach((did, attachList) -> {
            List<QueryDynamicAttachDTO> attachDTOList =
                    DynamicAttachConverter.getInstance().copyFieldValueList(attachList, QueryDynamicAttachDTO.class);
            dynamicInfoDTOMap.get(did).setAttachList(attachDTOList);
        });

//        批量查询统计数据
        userDynamicRepository.getDynamicStatsBatch(didList)
                .forEach(stats -> {
                    dynamicInfoDTOMap.get(stats.getDid()).setFavorCount(stats.getFavorCount());
                    dynamicInfoDTOMap.get(stats.getDid()).setRepostCount(stats.getRepostCount());
                });

        PageDTO<QueryDynamicInfoDTO> pageDTO = new PageDTO<>();
        pageDTO.setTotal(dynamicInfoPage.getTotal());
        pageDTO.setPageNo(dynamicInfoPage.getCurrent());
        pageDTO.setPageSize(dynamicInfoPage.getSize());
        pageDTO.setData(new ArrayList<>(dynamicInfoDTOMap.values()));
        return pageDTO;
    }
}
