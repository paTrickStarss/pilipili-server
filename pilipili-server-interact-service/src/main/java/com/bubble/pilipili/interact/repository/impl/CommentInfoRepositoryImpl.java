/*
 * Copyright (c) 2025. Bubble
 */

package com.bubble.pilipili.interact.repository.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bubble.pilipili.common.util.ListUtil;
import com.bubble.pilipili.interact.mapper.CommentInfoMapper;
import com.bubble.pilipili.interact.pojo.entity.CommentInfo;
import com.bubble.pilipili.interact.repository.CommentInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Bubble
 * @date 2025.03.05 16:19
 */
@Component
public class CommentInfoRepositoryImpl implements CommentInfoRepository {

    @Autowired
    private CommentInfoMapper commentInfoMapper;

    /**
     * 新增评论
     * @param commentInfo
     * @return
     */
    @Override
    public Boolean saveCommentInfo(CommentInfo commentInfo) {
        return commentInfoMapper.insert(commentInfo) == 1;
    }

    /**
     * 更新评论（少用）
     * @param commentInfo
     * @return
     */
    @Override
    public Boolean updateCommentInfo(CommentInfo commentInfo) {
        return commentInfoMapper.updateById(commentInfo) == 1;
    }

    /**
     * 删除评论
     * @param cid
     * @return
     */
    @Override
    public Boolean deleteCommentInfo(Integer cid) {
        return commentInfoMapper.deleteById(cid) == 1;
    }

    /**
     * 查询指定评论信息
     * @param cid
     * @return
     */
    @Override
    public CommentInfo getCommentInfo(Integer cid) {
        return commentInfoMapper.selectById(cid);
    }

    /**
     * 查询对象的所有评论（无深度）
     * @param relaType 1视频 2动态 3评论（回复）
     * @param relaId
     * @return
     */
    @Override
    public List<CommentInfo> queryCommentInfoByRela(Integer relaType, Integer relaId) {
        return commentInfoMapper.selectList(
                new LambdaQueryWrapper<CommentInfo>()
                        .eq(CommentInfo::getRelaType, relaType)
                        .eq(CommentInfo::getRelaId, relaId)
        );
    }

    /**
     * 分页查询某对象的所有直接评论（不包含评论回复）
     * @param relaType
     * @param relaId
     * @param pageNo
     * @param pageSize
     * @return
     */
    @Override
    public Page<CommentInfo> pageQueryCommentInfoByRela(Integer relaType, Integer relaId, Long pageNo, Long pageSize) {
        Page<CommentInfo> page = new Page<>(pageNo, pageSize);
        return commentInfoMapper.selectPage(page,
                new LambdaQueryWrapper<CommentInfo>()
                        .isNull(CommentInfo::getParentId)
                        .eq(CommentInfo::getRelaType, relaType)
                        .eq(CommentInfo::getRelaId, relaId)
        );
    }

    /**
     * 批量查询评论的回复数量
     * @param parentRootIdList
     * @return Map(cid, count)
     */
    @Override
    public Map<Long, Long> countCommentInfoByParentRootId(List<Integer> parentRootIdList) {
        if (ListUtil.isEmpty(parentRootIdList)) {
            return Collections.emptyMap();
        }

        QueryWrapper<CommentInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("parent_root_id AS cid", "count(1) AS count");
        queryWrapper.in("parent_root_id", parentRootIdList);
        queryWrapper.groupBy("parent_root_id");

        HashMap<Long, Long> result = new HashMap<>();
        /*
        * List<Map<String, Object>> :
        * [0] -> cid: xxx, count: xxx
        * [1] -> cid: xxx, count: xxx
        * [2] -> cid: xxx, count: xxx
        * ...
        * */
        commentInfoMapper.selectMaps(queryWrapper)
                .forEach(row ->
                        result.put((Long) row.get("cid"), (Long) row.get("count"))
                );

        /*
         * Map<String, Object> :
         * [0] -> cid: count
         * [1] -> cid: count
         * [2] -> cid: count
         * ...
         * */
        return result;


//        List<QueryCommentInfoCountDTO> collect = commentInfoMapper.selectMaps(queryWrapper).stream()
//                .map(map ->
//                        new QueryCommentInfoCountDTO((Integer) map.get("parent_root_id"), (Long) map.get("count"))
//                )
//                .collect(Collectors.toList());
//        return collect;
    }

    /**
     * 分页查询评论的所有回复
     * @param parentRootId
     * @param pageNo
     * @param pageSize
     * @return
     */
    @Override
    public Page<CommentInfo> pageQueryCommentInfoByParentRootId(Integer parentRootId, Long pageNo, Long pageSize) {
        Page<CommentInfo> page = new Page<>(pageNo, pageSize);
        return commentInfoMapper.selectPage(page,
                new LambdaQueryWrapper<CommentInfo>()
                        .eq(CommentInfo::getParentRootId, parentRootId)
        );
    }

}
