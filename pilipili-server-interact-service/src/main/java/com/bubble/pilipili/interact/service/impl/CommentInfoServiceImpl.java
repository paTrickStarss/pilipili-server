/*
 * Copyright (c) 2025. Bubble
 */

package com.bubble.pilipili.interact.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bubble.pilipili.common.pojo.PageDTO;
import com.bubble.pilipili.interact.pojo.converter.CommentInfoConverter;
import com.bubble.pilipili.interact.pojo.dto.QueryCommentInfoDTO;
import com.bubble.pilipili.interact.pojo.dto.QueryCommentStatsDTO;
import com.bubble.pilipili.interact.pojo.entity.CommentInfo;
import com.bubble.pilipili.interact.pojo.req.PageQueryCommentInfoReq;
import com.bubble.pilipili.interact.pojo.req.SaveCommentInfoReq;
import com.bubble.pilipili.interact.repository.CommentInfoRepository;
import com.bubble.pilipili.interact.repository.UserCommentRepository;
import com.bubble.pilipili.interact.service.CommentInfoService;
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
 * @date 2025.03.05 16:49
 */
@Service
public class CommentInfoServiceImpl implements CommentInfoService {

    @Autowired
    private CommentInfoRepository commentInfoRepository;
    @Autowired
    private UserCommentRepository userCommentRepository;

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
        Page<CommentInfo> commentInfoPage = commentInfoRepository.pageQueryCommentInfoByRela(req.getRelaType(), req.getRelaId(), req.getPageNo(), req.getPageSize());
        List<CommentInfo> records = commentInfoPage.getRecords();


        Map<Integer, QueryCommentInfoDTO> dtoMap = getCidMap(commentInfoPage.getRecords());

        // 查询每条评论的回复数量
        List<Integer> cidList = new ArrayList<>(dtoMap.keySet());
        Map<Long, Long> replyCountMap = commentInfoRepository.countCommentInfoByParentRootId(cidList);

        // 查询每条评论的统计数据
        List<QueryCommentStatsDTO> commentStats = userCommentRepository.getCommentStats(cidList);

        List<QueryCommentInfoDTO> resultDTOList = new ArrayList<>(dtoMap.values());
        for (QueryCommentInfoDTO dto : resultDTOList) {
            dto.setReplyCount(replyCountMap.get(dto.getCid().longValue()));
        }

        // todo: 绑定评论统计数据

        return new PageDTO<>(
                commentInfoPage.getCurrent(),
                commentInfoPage.getSize(),
                commentInfoPage.getTotal(),
                resultDTOList
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

        Map<Integer, QueryCommentInfoDTO> dtoMap = getCidMap(commentInfoPage.getRecords());

        // 查询并绑定评论统计数据
        List<Integer> cidList = new ArrayList<>(dtoMap.keySet());
        userCommentRepository.getCommentStats(cidList)
                .forEach(commentStats -> {
                    dtoMap.get(commentStats.getCid()).setFavorCount(commentStats.getFavorCount());
                    dtoMap.get(commentStats.getCid()).setDewCount(commentStats.getDewCount());
                });

        PageDTO<QueryCommentInfoDTO> pageDTO = new PageDTO<>();
        pageDTO.setPageNo(commentInfoPage.getCurrent());
        pageDTO.setPageSize(commentInfoPage.getSize());
        pageDTO.setTotal(commentInfoPage.getTotal());
        pageDTO.setData(new ArrayList<>(dtoMap.values()));
        return pageDTO;
    }

    /**
     * 按cid映射
     * @param commentInfoList
     * @return
     */
    private Map<Integer, QueryCommentInfoDTO> getCidMap(List<CommentInfo> commentInfoList) {
        return CommentInfoConverter.getInstance()
                .copyFieldValueList(commentInfoList, QueryCommentInfoDTO.class)
                .stream()
                .collect(Collectors.toMap(QueryCommentInfoDTO::getCid, Function.identity(),
                        (a, b) -> a)
                );
    }
}
