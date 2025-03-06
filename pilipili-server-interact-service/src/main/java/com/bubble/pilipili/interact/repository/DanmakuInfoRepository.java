/*
 * Copyright (c) 2025. Bubble
 */

package com.bubble.pilipili.interact.repository;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bubble.pilipili.interact.pojo.entity.DanmakuInfo;
import org.springframework.stereotype.Repository;

/**
 * @author Bubble
 * @date 2025.03.06 13:42
 */
@Repository
public interface DanmakuInfoRepository {

    Boolean saveDanmakuInfo(DanmakuInfo danmakuInfo);
    Boolean deleteDanmakuInfo(Integer danmakuId);

    Page<DanmakuInfo> pageQueryDanmakuInfoByUid(Integer uid, Long pageNo, Long pageSize);
    Page<DanmakuInfo> pageQueryDanmakuInfoByVid(Integer vid, Long pageNo, Long pageSize);
    Page<DanmakuInfo> pageQueryDanmakuInfoByVidAndUid(Integer vid, Integer uid, Long pageNo, Long pageSize);

}
