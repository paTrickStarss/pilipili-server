/*
 * Copyright (c) 2025. Bubble
 */

package com.bubble.pilipili.video.service;

import com.bubble.pilipili.common.pojo.PageDTO;
import com.bubble.pilipili.video.pojo.dto.QueryVideoInfoDTO;
import com.bubble.pilipili.video.pojo.req.CreateVideoInfoReq;
import com.bubble.pilipili.video.pojo.req.PageQueryVideoInfoReq;
import com.bubble.pilipili.video.pojo.req.UpdateVideoInfoReq;

/**
 * @author Bubble
 * @date 2025/01/21 15:28
 */
public interface VideoInfoService {

    Boolean saveVideoInfo(CreateVideoInfoReq req);
    Boolean updateVideoInfo(UpdateVideoInfoReq req);
    Boolean deleteVideoInfo(Integer vid);
    QueryVideoInfoDTO getVideoInfoById(Integer vid);
    PageDTO<QueryVideoInfoDTO> pageQueryVideoInfoByUid(PageQueryVideoInfoReq req);
    PageDTO<QueryVideoInfoDTO> pageQueryVideoInfo(PageQueryVideoInfoReq req);
}
