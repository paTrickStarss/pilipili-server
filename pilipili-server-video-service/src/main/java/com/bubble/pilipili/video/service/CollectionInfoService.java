/*
 * Copyright (c) 2025. Bubble
 */

package com.bubble.pilipili.video.service;

import com.bubble.pilipili.common.pojo.PageDTO;
import com.bubble.pilipili.video.pojo.dto.QueryCollectionInfoDTO;
import com.bubble.pilipili.video.pojo.req.PageQueryCollectionInfoReq;
import com.bubble.pilipili.video.pojo.req.SaveCollectionInfoReq;
import com.bubble.pilipili.video.pojo.req.UpdateCollectionInfoReq;
import org.springframework.stereotype.Service;

/**
 * @author Bubble
 * @date 2025.03.17 16:10
 */
@Service
public interface CollectionInfoService {

    /**
     * 保存收藏夹信息
     * @param req
     * @return
     */
    Boolean saveCollectionInfo(SaveCollectionInfoReq req);

    /**
     * 更新收藏夹信息
     * @param req
     * @return
     */
    Boolean updateCollectionInfo(UpdateCollectionInfoReq req);

    /**
     * 删除收藏夹
     * @param collectionId
     * @return
     */
    Boolean deleteCollectionInfo(Integer collectionId);

    /**
     * 查询指定收藏夹信息
     * @param collectionId
     * @return
     */
    QueryCollectionInfoDTO getCollectionInfo(Integer collectionId);

    /**
     * 分页查询用户收藏夹信息
     * @param req
     * @return
     */
    PageDTO<QueryCollectionInfoDTO> queryCollectionInfoByUid(PageQueryCollectionInfoReq req);
}
