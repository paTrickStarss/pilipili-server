/*
 * Copyright (c) 2025. Bubble
 */

package com.bubble.pilipili.interact.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bubble.pilipili.common.pojo.PageDTO;
import com.bubble.pilipili.common.util.ListUtil;
import com.bubble.pilipili.interact.pojo.converter.CommentInfoConverter;
import com.bubble.pilipili.interact.pojo.dto.QueryCommentInfoDTO;
import com.bubble.pilipili.interact.pojo.entity.CommentInfo;
import com.bubble.pilipili.interact.pojo.entity.CommentStats;
import com.bubble.pilipili.interact.pojo.entity.UserComment;
import com.bubble.pilipili.interact.pojo.req.PageQueryCommentInfoReq;
import com.bubble.pilipili.interact.pojo.req.SaveCommentInfoReq;
import com.bubble.pilipili.interact.repository.CommentInfoRepository;
import com.bubble.pilipili.interact.repository.CommentStatsRepository;
import com.bubble.pilipili.interact.repository.UserCommentRepository;
import com.bubble.pilipili.interact.service.CommentInfoService;
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
@Service
public class CommentInfoServiceImpl implements CommentInfoService {

    @Autowired
    private CommentInfoRepository commentInfoRepository;
    @Autowired
    private UserCommentRepository userCommentRepository;
    @Autowired
    private CommentStatsRepository commentStatsRepository;

    /**
     * @param req
     * @return
     */
    @Transactional
    @Override
    public Boolean saveCommentInfo(SaveCommentInfoReq req) {
        CommentInfo commentInfo = CommentInfoConverter.getInstance().copyFieldValue(req, CommentInfo.class);
        return commentInfoRepository.saveCommentInfo(commentInfo);
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
    @Override
    public Boolean favorCommentInfo(Integer cid, Integer uid) {
        return userCommentRepository.saveUserComment(
                generateUserComment(cid, uid, u -> {
                    u.setFavor(1);
                    u.setDew(0);
                })
        );
    }

    /**取消点赞评论
     * @param cid
     * @param uid
     * @return
     */
    @Override
    public Boolean revokeFavorCommentInfo(Integer cid, Integer uid) {
        return userCommentRepository.saveUserComment(
                generateUserComment(cid, uid, u -> u.setFavor(0))
        );
    }

    /**
     * 点踩评论
     * @param cid
     * @param uid
     * @return
     */
    @Override
    public Boolean dewCommentInfo(Integer cid, Integer uid) {
        return userCommentRepository.saveUserComment(
                generateUserComment(cid, uid, u -> {
                    u.setDew(1);
                    u.setFavor(0);
                })
        );
    }

    /**
     * 取消点踩评论
     * @param cid
     * @param uid
     * @return
     */
    @Override
    public Boolean revokeDewCommentInfo(Integer cid, Integer uid) {
        return userCommentRepository.saveUserComment(
                generateUserComment(cid, uid, u -> u.setDew(0))
        );
    }

    /**
     * @param cid
     * @return
     */
    @Override
    public QueryCommentInfoDTO queryCommentInfo(Integer cid) {
        CommentInfo commentInfo = commentInfoRepository.getCommentInfo(cid);
        return CommentInfoConverter.getInstance().copyFieldValue(commentInfo, QueryCommentInfoDTO.class);
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
     * 生成用户评论关系实体类
     * @param cid
     * @param uid
     * @param consumer
     * @return
     */
    private UserComment generateUserComment(
            Integer cid, Integer uid,
            Consumer<UserComment> consumer
    ) {
        UserComment userComment = new UserComment();
        userComment.setCid(cid);
        userComment.setUid(uid);
        consumer.accept(userComment);
        return userComment;
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
        Map<Integer, CommentStats> statsMap = commentStatsRepository.getStats(cidList);

        List<QueryCommentInfoDTO> dtoList = CommentInfoConverter.getInstance()
                .copyFieldValueList(commentInfoList, QueryCommentInfoDTO.class);

        dtoList.forEach(dto -> {
            CommentStats stats = statsMap.get(dto.getCid());
            dto.setFavorCount(stats.getFavorCount());
            dto.setDewCount(stats.getDewCount());
        });
        return dtoList;
    }
}
