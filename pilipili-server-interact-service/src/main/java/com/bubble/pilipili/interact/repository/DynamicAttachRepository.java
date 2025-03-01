/*
 * Copyright (c) 2025. Bubble
 */

package com.bubble.pilipili.interact.repository;

import com.bubble.pilipili.interact.pojo.entity.DynamicAttach;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @author Bubble
 * @date 2025.03.01 15:30
 */
@Repository
public interface DynamicAttachRepository {

    Boolean saveDynamicAttach(DynamicAttach dynamicAttach);
    Boolean saveDynamicAttachBatch(List<DynamicAttach> dynamicAttachList);
    Boolean deleteDynamicAttachByDid(Integer did);
    Boolean deleteDynamicAttachByUUID(Integer attachUUID);
    Boolean deleteDynamicAttachByUUIDBatch(List<String> attachUUIDList);

    List<DynamicAttach> listDynamicAttachByDid(Integer did);
    Map<Integer, List<DynamicAttach>> listDynamicAttachByDidBatch(List<Integer> didList);
}
