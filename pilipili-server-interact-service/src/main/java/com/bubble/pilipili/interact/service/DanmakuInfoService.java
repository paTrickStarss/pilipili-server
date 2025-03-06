/*
 * Copyright (c) 2025. Bubble
 */

package com.bubble.pilipili.interact.service;

import com.bubble.pilipili.common.pojo.PageDTO;
import com.bubble.pilipili.interact.pojo.dto.QueryDanmakuInfoDTO;
import com.bubble.pilipili.interact.pojo.req.PageQueryDanmakuInfoReq;
import com.bubble.pilipili.interact.pojo.req.SaveDanmakuInfoReq;

/**
 * @author Bubble
 * @date 2025.03.06 13:53
 */
public interface DanmakuInfoService {

    Boolean saveDanmakuInfo(SaveDanmakuInfoReq req);
    Boolean deleteDanmakuInfo(Integer danmakuId);

    Boolean favorDanmakuInfo(Integer danmakuId, Integer uid);
    Boolean revokeFavorDanmakuInfo(Integer danmakuId, Integer uid);
    Boolean dewDanmakuInfo(Integer danmakuId, Integer uid);
    Boolean revokeDewDanmakuInfo(Integer danmakuId, Integer uid);
    
    PageDTO<QueryDanmakuInfoDTO> pageQueryDanmakuInfoByVid(PageQueryDanmakuInfoReq req);
    PageDTO<QueryDanmakuInfoDTO> pageQueryDanmakuInfoByUid(PageQueryDanmakuInfoReq req);
    PageDTO<QueryDanmakuInfoDTO> pageQueryDanmakuInfoByVidAndUid(PageQueryDanmakuInfoReq req);
}
