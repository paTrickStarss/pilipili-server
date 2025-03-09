/*
 * Copyright (c) 2025. Bubble
 */

package com.bubble.pilipili.interact.repository.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.bubble.pilipili.common.exception.RepositoryException;
import com.bubble.pilipili.common.util.ListUtil;
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

        // todo: 批处理结果的处理方式可能有问题
        BatchResult batchResult = batchResultList.get(0);

        int sum = Arrays.stream(batchResult.getUpdateCounts()).sum();
        if (sum != dynamicAttachList.size()) {
            throw new RepositoryException("附件批量保存结果异常");
        }
        return true;
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
     * @param attachId
     * @return
     */
    @Override
    public Boolean deleteDynamicAttachByAttachId(Integer attachId) {
        return dynamicAttachMapper.deleteById(attachId) == 1;
    }

    /**
     * 删除指定动态附件信息
     * @param attachUUID
     * @return
     */
    @Override
    public Boolean deleteDynamicAttachByUUID(String attachUUID) {
        return dynamicAttachMapper.delete(
                new LambdaQueryWrapper<DynamicAttach>()
                        .eq(DynamicAttach::getAttachUUID, attachUUID)
        ) >= 0;
    }

    /**
     * 批量删除动态附件信息
     * @param attachUUIDList
     * @return
     */
    @Override
    public Boolean deleteDynamicAttachByUUID(List<String> attachUUIDList) {
        if (ListUtil.isEmpty(attachUUIDList)) {
            return false;
        }
        return dynamicAttachMapper.delete(
                new LambdaQueryWrapper<DynamicAttach>()
                        .in(DynamicAttach::getAttachUUID, attachUUIDList)
        ) == attachUUIDList.size();
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
    public Map<Integer, List<DynamicAttach>> listDynamicAttachByDid(List<Integer> didList) {
        if (didList == null || didList.isEmpty()) {
            return new HashMap<>(0);
        }
        List<DynamicAttach> attachListUngrouped =
                dynamicAttachMapper.selectList(
                        new LambdaQueryWrapper<DynamicAttach>()
                                .in(DynamicAttach::getDid, didList)
                );

        return attachListUngrouped.stream().collect(Collectors.groupingBy(DynamicAttach::getDid));
    }
}
