/*
 * Copyright (c) 2025. Bubble
 */

package com.bubble.pilipili.interact.repository;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bubble.pilipili.interact.pojo.entity.DynamicInfo;
import org.springframework.stereotype.Repository;

/**
 * @author Bubble
 * @date 2025.03.01 16:40
 */
@Repository
public interface DynamicInfoRepository {

    Boolean saveDynamicInfo(DynamicInfo dynamicInfo);
    Boolean updateDynamicInfo(DynamicInfo dynamicInfo);
    Boolean deleteDynamicInfoByDid(Integer did);

    DynamicInfo queryDynamicInfoByDid(Integer did);
    Page<DynamicInfo> pageQueryDynamicInfoByUid(Integer uid, Long pageNo, Long pageSize);
}
