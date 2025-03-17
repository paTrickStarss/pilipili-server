/*
 * Copyright (c) 2025. Bubble
 */

package com.bubble.pilipili.video.service;

import com.bubble.pilipili.common.pojo.PageDTO;
import com.bubble.pilipili.video.pojo.dto.QueryVideoInfoDTO;
import com.bubble.pilipili.video.pojo.req.ChangeCollectionVideoReq;
import com.bubble.pilipili.video.pojo.req.PageQueryCollectionVideoReq;
import org.springframework.stereotype.Service;

/**
 * @author Bubble
 * @date 2025.03.17 17:08
 */
@Service
public interface CollectionVideoService {

    /**
     * 批量保存收藏夹视频关系
     * @param req
     * @return
     */
    Boolean saveCollectionVideo(ChangeCollectionVideoReq req);

    /**
     * 批量删除收藏夹视频关系
     * @param req
     * @return
     */
    Boolean deleteCollectionVideo(ChangeCollectionVideoReq req);

    /**
     * 分页查询收藏夹视频
     * @param req
     * @return
     */
    PageDTO<QueryVideoInfoDTO> queryCollectionVideo(PageQueryCollectionVideoReq req);

}
