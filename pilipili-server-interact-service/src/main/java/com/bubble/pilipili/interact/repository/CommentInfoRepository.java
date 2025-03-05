/*
 * Copyright (c) 2025. Bubble
 */

package com.bubble.pilipili.interact.repository;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bubble.pilipili.interact.pojo.entity.CommentInfo;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @author Bubble
 * @date 2025.03.05 16:09
 */
@Repository
public interface CommentInfoRepository {

    Boolean saveCommentInfo(CommentInfo commentInfo);
    Boolean updateCommentInfo(CommentInfo commentInfo);
    Boolean deleteCommentInfo(Integer cid);

    CommentInfo getCommentInfo(Integer cid);
    List<CommentInfo> queryCommentInfoByRela(Integer relaType, Integer relaId);

    Page<CommentInfo> pageQueryCommentInfoByRela(Integer relaType, Integer relaId, Long pageNo, Long pageSize);
    Map<Long, Long> countCommentInfoByParentRootId(List<Integer> parentRootIdList);

    Page<CommentInfo> pageQueryCommentInfoByParentRootId(Integer parentRootId, Long pageNo, Long pageSize);
}
