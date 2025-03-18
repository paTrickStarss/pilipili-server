/*
 * Copyright (c) 2025. Bubble
 */

package com.bubble.pilipili.interact.service;

import com.bubble.pilipili.common.pojo.PageDTO;
import com.bubble.pilipili.interact.pojo.dto.QueryDynamicInfoDTO;
import com.bubble.pilipili.interact.pojo.req.PageQueryDynamicInfoReq;
import com.bubble.pilipili.interact.pojo.req.SaveDynamicInfoReq;
import com.bubble.pilipili.interact.pojo.req.UpdateDynamicInfoReq;

/**
 * @author Bubble
 * @date 2025.03.01 16:13
 */
public interface DynamicInfoService {

    Boolean saveDynamicInfo(SaveDynamicInfoReq req);
    Boolean updateDynamicInfo(UpdateDynamicInfoReq req);
    Boolean deleteDynamicInfo(Integer did);
    Boolean favorDynamicInfo(Integer did, Integer uid);
    Boolean revokeFavorDynamicInfo(Integer did, Integer uid);
    Boolean commentDynamicInfo(Integer did, Integer uid);
    Boolean repostDynamicInfo(Integer did, Integer uid);

    QueryDynamicInfoDTO queryDynamicInfoDTO(Integer did);
    PageDTO<QueryDynamicInfoDTO> pageQueryDynamicInfoByUid(PageQueryDynamicInfoReq req);
}
