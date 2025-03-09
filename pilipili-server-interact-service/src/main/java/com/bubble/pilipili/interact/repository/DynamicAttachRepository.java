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

    /**
     * 保存动态附件信息
     * @param dynamicAttach
     * @return
     */
    Boolean saveDynamicAttach(DynamicAttach dynamicAttach);

    /**
     * 批量保存动态附件信息
     * @param dynamicAttachList
     * @return
     */
    Boolean saveDynamicAttachBatch(List<DynamicAttach> dynamicAttachList);

    /**
     * 删除指定动态的所有附件信息
     * @param did
     * @return
     */
    Boolean deleteDynamicAttachByDid(Integer did);

    /**
     * 删除指定动态附件信息
     * @param attachId
     * @return
     */
    Boolean deleteDynamicAttachByAttachId(Integer attachId);

    /**
     * 删除指定动态附件信息
     * @param attachUUID
     * @return
     */
    Boolean deleteDynamicAttachByUUID(String attachUUID);

    /**
     * 批量删除动态附件信息
     * @param attachUUIDList
     * @return
     */
    Boolean deleteDynamicAttachByUUID(List<String> attachUUIDList);

    /**
     * 查询指定动态的所有附件信息
     * @param did
     * @return
     */
    List<DynamicAttach> listDynamicAttachByDid(Integer did);

    /**
     * 批量查询指定动态的所有附件信息
     * @param didList
     * @return Map&lt;did, attachList&gt;
     */
    Map<Integer, List<DynamicAttach>> listDynamicAttachByDid(List<Integer> didList);
}
