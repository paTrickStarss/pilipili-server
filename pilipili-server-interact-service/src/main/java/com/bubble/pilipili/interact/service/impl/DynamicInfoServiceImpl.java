/*
 * Copyright (c) 2025. Bubble
 */

package com.bubble.pilipili.interact.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bubble.pilipili.common.exception.RepositoryException;
import com.bubble.pilipili.common.exception.ServiceOperationException;
import com.bubble.pilipili.common.http.SimpleResponse;
import com.bubble.pilipili.common.pojo.PageDTO;
import com.bubble.pilipili.common.service.InteractStatsAction;
import com.bubble.pilipili.common.util.ListUtil;
import com.bubble.pilipili.feign.api.StatsFeignAPI;
import com.bubble.pilipili.feign.api.StatsMQFeignAPI;
import com.bubble.pilipili.feign.pojo.dto.QueryStatsDTO;
import com.bubble.pilipili.feign.pojo.req.SendDynamicStatsReq;
import com.bubble.pilipili.interact.pojo.converter.DynamicAttachConverter;
import com.bubble.pilipili.interact.pojo.converter.DynamicInfoConverter;
import com.bubble.pilipili.interact.pojo.dto.QueryDynamicAttachDTO;
import com.bubble.pilipili.interact.pojo.dto.QueryDynamicInfoDTO;
import com.bubble.pilipili.interact.pojo.entity.DynamicAttach;
import com.bubble.pilipili.interact.pojo.entity.DynamicInfo;
import com.bubble.pilipili.feign.pojo.entity.DynamicStats;
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
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
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
    @Autowired
    private InteractStatsAction interactStatsAction;

    @Autowired
    private StatsFeignAPI statsFeignAPI;
    @Autowired
    private StatsMQFeignAPI statsMQFeignAPI;

    @Autowired
    private ThreadPoolTaskExecutor bubbleThreadPool;

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
                Boolean removeAttach = dynamicAttachRepository.deleteDynamicAttachByUUID(removeUUIDList);
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
    @Transactional
    @Override
    public Boolean deleteDynamicInfo(Integer did) {
        Boolean b = dynamicAttachRepository.deleteDynamicAttachByDid(did);
        if (!b) {
            throw new ServiceOperationException("动态附件删除失败");
        }
        return dynamicInfoRepository.deleteDynamicInfoByDid(did);
    }


    /**
     * 点赞动态
     * @param did
     * @param uid
     * @return
     */
    @Transactional
    @Override
    public Boolean favorDynamicInfo(Integer did, Integer uid) {
        return updateDynamicInteract(
                did, uid,
                ud -> ud.setFavor(1),
                stats -> stats.setFavorCount(1L)
        );
    }

    /**
     * 取消点赞动态
     * @param did
     * @param uid
     * @return
     */
    @Transactional
    @Override
    public Boolean revokeFavorDynamicInfo(Integer did, Integer uid) {
        return updateDynamicInteract(
                did, uid,
                ud -> ud.setFavor(0),
                stats -> stats.setFavorCount(-1L)
        );
    }

    /**
     * 评论动态
     * @param did
     * @param uid
     * @return
     */
    @Transactional
    @Override
    public Boolean commentDynamicInfo(Integer did, Integer uid) {
        return updateDynamicInteract(
                did, uid,
                ud -> ud.setComment(1),
                stats -> stats.setCommentCount(1L)
        );
    }

    /**
     * 转发动态
     * @param did
     * @param uid
     * @return
     */
    @Transactional
    @Override
    public Boolean repostDynamicInfo(Integer did, Integer uid) {
        return updateDynamicInteract(
                did, uid,
                ud -> ud.setRepost(1),
                stats -> stats.setRepostCount(1L)
        );
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
        SimpleResponse<QueryStatsDTO<DynamicStats>> response =
                statsFeignAPI.getDynamicStats(Collections.singletonList(did));
        response.getData().getStatsMap().get(0);
        DynamicStats stats = response.getData().getStatsMap().get(0);

        QueryDynamicInfoDTO dto =
                DynamicInfoConverter.getInstance().copyFieldValue(dynamicInfo, QueryDynamicInfoDTO.class);
        dto.setAttachList(
                DynamicAttachConverter.getInstance().copyFieldValueList(attachList, QueryDynamicAttachDTO.class)
        );

        if (stats != null) {
            dto.setFavorCount(stats.getFavorCount());
            dto.setCommentCount(stats.getCommentCount());
            dto.setRepostCount(stats.getRepostCount());
        }

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

        List<QueryDynamicInfoDTO> dtoList = handleDynamicInfo(dynamicInfoPage.getRecords());

        return PageDTO.createPageDTO(dynamicInfoPage, dtoList);
    }

    /**
     * 查询动态附件、统计数据，并装填DTO
     * @param dynamicInfoList
     * @return
     */
    private List<QueryDynamicInfoDTO> handleDynamicInfo(List<DynamicInfo> dynamicInfoList) {
        if (ListUtil.isEmpty(dynamicInfoList)) {
            return Collections.emptyList();
        }

        List<Integer> didList = dynamicInfoList.stream().map(DynamicInfo::getDid).collect(Collectors.toList());
        // 查询统计数据
        Map<Integer, DynamicStats> statsMap = statsFeignAPI.getDynamicStats(didList).getData().getStatsMap();
        // 查询附件信息
        Map<Integer, List<DynamicAttach>> attachMap =
                dynamicAttachRepository.listDynamicAttachByDid(didList);

        List<QueryDynamicInfoDTO> dtoList =
                DynamicInfoConverter.getInstance().copyFieldValueList(dynamicInfoList, QueryDynamicInfoDTO.class);
        dtoList.forEach(dto -> {
            DynamicStats stats = statsMap.get(dto.getDid());
            if (stats != null) {
                dto.setFavorCount(stats.getFavorCount());
                dto.setCommentCount(stats.getCommentCount());
                dto.setRepostCount(stats.getRepostCount());
            }

            List<DynamicAttach> dynamicAttachList = attachMap.get(dto.getDid());
            if (ListUtil.isNotEmpty(dynamicAttachList)) {
                List<QueryDynamicAttachDTO> attachDTOList = DynamicAttachConverter.getInstance()
                        .copyFieldValueList(dynamicAttachList, QueryDynamicAttachDTO.class);
                dto.setAttachList(attachDTOList);
            }
        });

        return dtoList;
    }


    /**
     * 更新用户动态互动关系数据
     * @param did
     * @param uid
     * @param interactConsumer
     * @param statsConsumer
     * @return
     */
    private Boolean updateDynamicInteract(
            Integer did, Integer uid,
            Consumer<UserDynamic> interactConsumer,
            Consumer<DynamicStats> statsConsumer
    ) {
        try {
            Boolean b = interactStatsAction.updateInteract(
                    UserDynamic.class,
                    did, uid,
                    UserDynamic::setDid,
                    userDynamicRepository,
                    interactConsumer
            );
            // todo: 推送动态统计数据更新信息
            if (b) {
                DynamicStats stats = new DynamicStats();
                statsConsumer.accept(stats);
                SendDynamicStatsReq req =
                        DynamicInfoConverter.getInstance().copyFieldValue(stats, SendDynamicStatsReq.class);
                req.setDid(did);
                statsMQFeignAPI.sendDynamicStats(req);
            }
            return b;
        } catch (Exception e) {
            log.warn(e.getMessage());
            throw new ServiceOperationException("更新用户动态互动关系数据异常");
        }
    }
}
