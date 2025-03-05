/*
 * Copyright (c) 2025. Bubble
 */

package com.bubble.pilipili.interact.service;

import com.bubble.pilipili.common.pojo.PageDTO;
import com.bubble.pilipili.interact.pojo.dto.QueryCommentInfoDTO;
import com.bubble.pilipili.interact.pojo.req.PageQueryCommentInfoReq;
import com.bubble.pilipili.interact.pojo.req.SaveCommentInfoReq;

/**
 * @author Bubble
 * @date 2025.03.05 16:38
 */
public interface CommentInfoService {

    Boolean saveCommentInfo(SaveCommentInfoReq req);
    Boolean deleteCommentInfo(Integer cid);

    QueryCommentInfoDTO queryCommentInfo(Integer cid);
    PageDTO<QueryCommentInfoDTO> pageQueryCommentInfoByRela(PageQueryCommentInfoReq req);
    PageDTO<QueryCommentInfoDTO> pageQueryCommentReply(PageQueryCommentInfoReq req);
}
