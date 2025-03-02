/*
 * Copyright (c) 2025. Bubble
 */

package com.bubble.pilipili.interact.repository.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.bubble.pilipili.interact.mapper.DynamicAttachMapper;
import com.bubble.pilipili.interact.pojo.entity.DynamicAttach;
import com.bubble.pilipili.interact.repository.DynamicAttachRepository;
import org.apache.ibatis.executor.BatchResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Bubble
 * @date 2025.03.01 16:00
 */
@Component
public class DynamicAttachRepositoryImpl implements DynamicAttachRepository {

    @Autowired
    private DynamicAttachMapper dynamicAttachMapper;

    /**
     * 保存动态附件信息
     * @param dynamicAttach
     * @return
     */
    @Override
    public Boolean saveDynamicAttach(DynamicAttach dynamicAttach) {
        return dynamicAttachMapper.insert(dynamicAttach) == 1;
    }

    /**
     * 批量保存动态附件信息
     * @param dynamicAttachList
     * @return
     */
    @Override
    public Boolean saveDynamicAttachBatch(List<DynamicAttach> dynamicAttachList) {
        List<BatchResult> batchResultList = dynamicAttachMapper.insert(dynamicAttachList);
        BatchResult batchResult = batchResultList.get(0);
        int sum = Arrays.stream(batchResult.getUpdateCounts()).sum();
        return sum == dynamicAttachList.size();
    }

    /**
     * 删除指定动态的所有附件信息
     * @param did
     * @return
     */
    @Override
    public Boolean deleteDynamicAttachByDid(Integer did) {
        return dynamicAttachMapper.delete(
                new LambdaQueryWrapper<DynamicAttach>()
                        .eq(DynamicAttach::getDid, did)
        ) >= 0;
    }

    /**
     * 删除指定动态附件信息
     * @param attachUUID
     * @return
     */
    @Override
    public Boolean deleteDynamicAttachByUUID(Integer attachUUID) {
        return dynamicAttachMapper.delete(
                new LambdaQueryWrapper<DynamicAttach>()
                        .eq(DynamicAttach::getAttachUUID, attachUUID)
        ) >= 0;
    }

    /**
     * @param attachUUIDList
     * @return
     */
    @Override
    public Boolean deleteDynamicAttachByUUIDBatch(List<String> attachUUIDList) {
        return dynamicAttachMapper.deleteByIds(attachUUIDList) == attachUUIDList.size();
    }


    /**
     * 查询指定动态的所有附件信息
     * @param did
     * @return
     */
    @Override
    public List<DynamicAttach> listDynamicAttachByDid(Integer did) {
        return dynamicAttachMapper.selectList(
                new LambdaQueryWrapper<DynamicAttach>()
                        .eq(DynamicAttach::getDid, did)
        );
    }

    /**
     * 批量查询指定动态的所有附件信息
     * @param didList
     * @return Map&lt;did, attachList&gt;
     */
    @Override
    public Map<Integer, List<DynamicAttach>>  listDynamicAttachByDidBatch(List<Integer> didList) {
        if (didList == null || didList.isEmpty()) {
            return new HashMap<>(0);
        }
        List<DynamicAttach> attachListUngrouped =
                dynamicAttachMapper.selectList(
                        new LambdaQueryWrapper<DynamicAttach>().in(DynamicAttach::getDid, didList));

        return attachListUngrouped.stream().collect(Collectors.groupingBy(DynamicAttach::getDid));
    }
}
