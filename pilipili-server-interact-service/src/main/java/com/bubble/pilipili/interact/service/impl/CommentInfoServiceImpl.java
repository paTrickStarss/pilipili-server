/*
 * Copyright (c) 2025. Bubble
 */

package com.bubble.pilipili.interact.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bubble.pilipili.common.exception.ServiceOperationException;
import com.bubble.pilipili.common.http.SimpleResponse;
import com.bubble.pilipili.common.pojo.PageDTO;
import com.bubble.pilipili.common.service.InteractStatsAction;
import com.bubble.pilipili.common.util.ListUtil;
import com.bubble.pilipili.feign.api.DynamicFeignAPI;
import com.bubble.pilipili.feign.api.StatsFeignAPI;
import com.bubble.pilipili.feign.api.MQFeignAPI;
import com.bubble.pilipili.feign.pojo.req.SendCommentStatsReq;
import com.bubble.pilipili.interact.pojo.converter.CommentInfoConverter;
import com.bubble.pilipili.interact.pojo.dto.QueryCommentInfoDTO;
import com.bubble.pilipili.interact.pojo.entity.CommentInfo;
import com.bubble.pilipili.feign.pojo.entity.CommentStats;
import com.bubble.pilipili.interact.pojo.entity.UserComment;
import com.bubble.pilipili.interact.pojo.req.PageQueryCommentInfoReq;
import com.bubble.pilipili.interact.pojo.req.SaveCommentInfoReq;
import com.bubble.pilipili.interact.repository.CommentInfoRepository;
import com.bubble.pilipili.interact.repository.UserCommentRepository;
import com.bubble.pilipili.interact.service.CommentInfoService;
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
 * @date 2025.03.05 16:49
 */
@Slf4j
@Service
public class CommentInfoServiceImpl implements CommentInfoService {

    @Autowired
    private CommentInfoRepository commentInfoRepository;
    @Autowired
    private UserCommentRepository userCommentRepository;
    @Autowired
    private InteractStatsAction interactStatsAction;


    @Autowired
    private MQFeignAPI MQFeignAPI;
    @Autowired
    private StatsFeignAPI statsFeignAPI;
    @Autowired
    private DynamicFeignAPI dynamicFeignAPI;

    /**
     * @param req
     * @return
     */
    @Transactional
    @Override
    public Boolean saveCommentInfo(SaveCommentInfoReq req) {
        CommentInfo commentInfo = CommentInfoConverter.getInstance().copyFieldValue(req, CommentInfo.class);
        Boolean b = commentInfoRepository.saveCommentInfo(commentInfo);

        updateTargetStats(req.getUid(), req.getRelaType(), req.getRelaId());

        return b;
    }

    /**
     * @param cid
     * @return
     */
    @Transactional
    @Override
    public Boolean deleteCommentInfo(Integer cid) {
        return commentInfoRepository.deleteCommentInfo(cid);
    }

    /**
     * 点赞评论
     * @param cid
     * @param uid
     * @return
     */
    @Transactional
    @Override
    public Boolean favorCommentInfo(Integer cid, Integer uid) {
        // 当第一次保存时，表中不存在该条stats数据，这条revoke会更新一条"dew_count = -1"的统计数据导致报错
//        revokeDewCommentInfo(cid, uid);
        Boolean b = updateCommentInteract(
                cid, uid,
                uc -> uc.setFavor(1),
                stats -> stats.setFavorCount(1L)
        );
        // 放到这里可以解决上面的问题
        revokeDewCommentInfo(cid, uid);
        return b;
    }

    /**取消点赞评论
     * @param cid
     * @param uid
     * @return
     */
    @Transactional
    @Override
    public Boolean revokeFavorCommentInfo(Integer cid, Integer uid) {
        return updateCommentInteract(
                cid, uid,
                uc -> uc.setFavor(0),
                stats -> stats.setFavorCount(-1L)
        );
    }

    /**
     * 点踩评论
     * @param cid
     * @param uid
     * @return
     */
    @Transactional
    @Override
    public Boolean dewCommentInfo(Integer cid, Integer uid) {
        Boolean b = updateCommentInteract(
                cid, uid,
                uc -> uc.setDew(1),
                stats -> stats.setDewCount(1L)
        );
        revokeFavorCommentInfo(cid, uid);
        return b;
    }

    /**
     * 取消点踩评论
     * @param cid
     * @param uid
     * @return
     */
    @Transactional
    @Override
    public Boolean revokeDewCommentInfo(Integer cid, Integer uid) {
        return updateCommentInteract(
                cid, uid,
                uc -> uc.setDew(0),
                stats -> stats.setDewCount(-1L)
        );
    }

    /**
     * @param cid
     * @return
     */
    @Override
    public QueryCommentInfoDTO queryCommentInfo(Integer cid) {
        CommentInfo commentInfo = commentInfoRepository.getCommentInfo(cid);
        if (commentInfo == null) {
            return null;
        }
        return handleCommentInfo(Collections.singletonList(commentInfo)).get(0);
    }

    /**
     * 查询对象的所有评论（不包含评论回复）
     * @param req
     * @return
     */
    @Override
    public PageDTO<QueryCommentInfoDTO> pageQueryCommentInfoByRela(PageQueryCommentInfoReq req) {
        Page<CommentInfo> commentInfoPage =
                commentInfoRepository.pageQueryCommentInfoByRela(
                        req.getRelaType(), req.getRelaId(), req.getPageNo(), req.getPageSize()
                );

        List<QueryCommentInfoDTO> dtoList = handleCommentInfo(commentInfoPage.getRecords());
        queryCommentReplyCount(dtoList);

        return new PageDTO<>(
                commentInfoPage.getCurrent(),
                commentInfoPage.getSize(),
                commentInfoPage.getTotal(),
                dtoList
        );
    }

    /**
     * 分页查询指定评论的回复
     * @param req
     * @return
     */
    @Override
    public PageDTO<QueryCommentInfoDTO> pageQueryCommentReply(PageQueryCommentInfoReq req) {
        Page<CommentInfo> commentInfoPage =
                commentInfoRepository.pageQueryCommentInfoByParentRootId(
                        req.getParentRootId(), req.getPageNo(), req.getPageSize()
                );

        List<QueryCommentInfoDTO> dtoList = handleCommentInfo(commentInfoPage.getRecords());

        return new PageDTO<>(
                commentInfoPage.getCurrent(),
                commentInfoPage.getSize(),
                commentInfoPage.getTotal(),
                dtoList
        );
    }

    /**
     * 查询统计数据并装填dto
     * @param commentInfoList
     * @return
     */
    private List<QueryCommentInfoDTO> handleCommentInfo(List<CommentInfo> commentInfoList) {
        if (ListUtil.isEmpty(commentInfoList)) {
            return Collections.emptyList();
        }

        List<Integer> cidList = commentInfoList.stream().map(CommentInfo::getCid).collect(Collectors.toList());
        Map<Integer, CommentStats> statsMap = statsFeignAPI.getCommentStats(cidList).getData().getStatsMap();

        List<QueryCommentInfoDTO> dtoList = CommentInfoConverter.getInstance()
                .copyFieldValueList(commentInfoList, QueryCommentInfoDTO.class);

        dtoList.forEach(dto -> {
            CommentStats stats = statsMap.get(dto.getCid());
            if (stats != null) {
                dto.setFavorCount(stats.getFavorCount());
                dto.setDewCount(stats.getDewCount());
            }
        });
        return dtoList;
    }

    /**
     * 查询评论回复数量
     * @param dtoList
     */
    private void queryCommentReplyCount(List<QueryCommentInfoDTO> dtoList) {
        if (ListUtil.isEmpty(dtoList)) {
            return;
        }

        List<Integer> cidList = dtoList.stream().map(QueryCommentInfoDTO::getCid).collect(Collectors.toList());
        Map<Long, Long> replyCountMap = commentInfoRepository.countCommentInfoByParentRootId(cidList);
        dtoList.forEach(dto -> {
            Long count = replyCountMap.get(new Long(dto.getCid()));
            dto.setReplyCount(count == null ? 0 : count);
        });

    }

    /**
     * 更新评论互动关系数据
     * @param cid
     * @param uid
     * @param interactConsumer
     * @param statsConsumer
     * @return
     */
    private Boolean updateCommentInteract(
            Integer cid, Integer uid,
            Consumer<UserComment> interactConsumer,
            Consumer<CommentStats> statsConsumer
    ) {
        try {
            Boolean b = interactStatsAction.updateInteract(
                    UserComment.class,
                    cid, uid,
                    UserComment::setCid,
                    userCommentRepository,
                    interactConsumer
            );
            // todo: 推送评论统计数据更新信息
            if (b) {
                CommentStats stats = new CommentStats();
                statsConsumer.accept(stats);
                SendCommentStatsReq req =
                        CommentInfoConverter.getInstance().copyFieldValue(stats, SendCommentStatsReq.class);
                req.setCid(cid);
                MQFeignAPI.sendCommentStats(req);
            }
            return b;
        } catch (Exception e) {
            log.warn(e.getMessage());
            throw new ServiceOperationException("更新评论互动关系数据异常");
        }
    }

    /**
     * 更新评论对象统计数据
     * @param uid 发送用户ID
     * @param relaType 评论对象类型 1视频 2动态 3评论（回复）
     * @param relaId 评论对象ID
     */
    private void updateTargetStats(Integer uid, Integer relaType, Integer relaId) {
        SimpleResponse<String> response = null;
        switch (relaType) {
            case 1:
                // video
                // todo: 更新视频评论统计数据
                break;
            case 2:
                // dynamic
                response = dynamicFeignAPI.comment(relaId, uid);
                break;
            case 3:
                // comment reply

                break;
            default: break;
        }

        if (response == null) {
            log.warn("更新评论对象统计数据失败");
        } else if (response.isSuccess()) {
            log.info("更新评论对象统计数据成功");
        }
    }

}
