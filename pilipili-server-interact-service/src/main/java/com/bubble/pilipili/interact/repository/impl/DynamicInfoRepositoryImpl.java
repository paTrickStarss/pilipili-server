/*
 * Copyright (c) 2025. Bubble
 */

package com.bubble.pilipili.interact.repository.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bubble.pilipili.interact.mapper.DynamicInfoMapper;
import com.bubble.pilipili.interact.pojo.entity.DynamicInfo;
import com.bubble.pilipili.interact.repository.DynamicInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author Bubble
 * @date 2025.03.01 16:43
 */
@Component
public class DynamicInfoRepositoryImpl implements DynamicInfoRepository {

    @Autowired
    private DynamicInfoMapper dynamicInfoMapper;

    /**
     * @param dynamicInfo
     * @return
     */
    @Override
    public Boolean saveDynamicInfo(DynamicInfo dynamicInfo) {
        return dynamicInfoMapper.insert(dynamicInfo) == 1;
    }

    /**
     * @param dynamicInfo
     * @return
     */
    @Override
    public Boolean updateDynamicInfo(DynamicInfo dynamicInfo) {
        return dynamicInfoMapper.updateById(dynamicInfo) == 1;
    }

    /**
     * @param did
     * @return
     */
    @Override
    public Boolean deleteDynamicInfoByDid(Integer did) {
        return dynamicInfoMapper.deleteById(did) == 1;
    }

    /**
     * 查询指定动态信息
     * @param did
     * @return
     */
    @Override
    public DynamicInfo queryDynamicInfoByDid(Integer did) {
        return dynamicInfoMapper.selectById(did);
    }

    /**
     * 分页查询指定用户的动态
     * @param uid
     * @param pageNo
     * @param pageSize
     * @return
     */
    @Override
    public Page<DynamicInfo> pageQueryDynamicInfoByUid(Integer uid, Long pageNo, Long pageSize) {
        Page<DynamicInfo> page = new Page<>(pageNo, pageSize);
        return dynamicInfoMapper.selectPage(page,
                new LambdaQueryWrapper<DynamicInfo>()
                        .eq(DynamicInfo::getUid, uid)
                        .eq(DynamicInfo::getRm, 0)
                        .orderBy(true, false, DynamicInfo::getCreateTime)
        );
    }
}
