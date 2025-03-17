/*
 * Copyright (c) 2025. Bubble
 */

package com.bubble.pilipili.video.repository.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bubble.pilipili.video.mapper.CollectionInfoMapper;
import com.bubble.pilipili.video.pojo.entity.CollectionInfo;
import com.bubble.pilipili.video.repository.CollectionInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 * @author Bubble
 * @date 2025.03.17 15:46
 */
@Repository
public class CollectionInfoRepositoryImpl implements CollectionInfoRepository {

    @Autowired
    private CollectionInfoMapper collectionInfoMapper;

    /**
     * 保存收藏夹信息
     *
     * @param collectionInfo
     * @return
     */
    @Override
    public Boolean save(CollectionInfo collectionInfo) {
        return collectionInfoMapper.insert(collectionInfo) == 1;
    }

    /**
     * 更新收藏夹信息
     *
     * @param collectionInfo
     * @return
     */
    @Override
    public Boolean update(CollectionInfo collectionInfo) {
        return collectionInfoMapper.updateById(collectionInfo) == 1;
    }

    /**
     * 删除收藏夹
     *
     * @param collectionId
     * @return
     */
    @Override
    public Boolean delete(Integer collectionId) {
        return collectionInfoMapper.deleteById(collectionId) == 1;
    }

    /**
     * 查询指定收藏夹信息
     *
     * @param collectionId
     * @return
     */
    @Override
    public CollectionInfo query(Integer collectionId) {
        return collectionInfoMapper.selectOne(
                new LambdaQueryWrapper<CollectionInfo>()
                        .eq(CollectionInfo::getCollectionId, collectionId)
        );
    }

    /**
     * 分页查询用户收藏夹
     *
     * @param uid
     * @param pageNo
     * @param pageSize
     * @return
     */
    @Override
    public Page<CollectionInfo> queryByUid(Integer uid, Long pageNo, Long pageSize) {
        Page<CollectionInfo> page = new Page<>(pageNo, pageSize);
        return collectionInfoMapper.selectPage(page,
                new LambdaQueryWrapper<CollectionInfo>()
                        .eq(CollectionInfo::getUid, uid)
        );
    }
}
