/*
 * Copyright (c) 2025. Bubble
 */

package com.bubble.pilipili.video.repository.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bubble.pilipili.common.exception.RepositoryException;
import com.bubble.pilipili.common.util.ListUtil;
import com.bubble.pilipili.video.mapper.CollectionVideoMapper;
import com.bubble.pilipili.video.pojo.entity.CollectionVideo;
import com.bubble.pilipili.video.repository.CollectionVideoRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.exceptions.PersistenceException;
import org.apache.ibatis.executor.BatchResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Repository;

import java.util.*;

/**
 * @author Bubble
 * @date 2025.03.17 16:00
 */
@Slf4j
@Repository
public class CollectionVideoRepositoryImpl implements CollectionVideoRepository {

    @Autowired
    private CollectionVideoMapper collectionVideoMapper;

    /**
     * 保存收藏夹视频关系
     *
     * @param collectionVideo
     * @return
     */
    @Override
    public Boolean save(CollectionVideo collectionVideo) {
        if (!exists(collectionVideo)) {
            collectionVideoMapper.insert(collectionVideo);
        }
        return true;
    }

    /**
     * 保存收藏夹视频关系
     *
     * @param collectionVideoList
     * @return
     */
    @Override
    public Boolean save(List<CollectionVideo> collectionVideoList) {
        if (ListUtil.isEmpty(collectionVideoList)) {
            log.info("No data to insert, returning true.");
            return true;
        }
        List<BatchResult> batchResultList = Collections.emptyList();
        try {
            batchResultList = collectionVideoMapper.insert(collectionVideoList);
        } catch (PersistenceException ex) {
            log.warn("Collection video already exists, returning true.\n{}", ex.getMessage());
        }

        if (batchResultList.isEmpty()) {
            return true;
        }
        BatchResult batchResult = batchResultList.get(0);
        int[] updateCounts = batchResult.getUpdateCounts();
        int count = Arrays.stream(updateCounts).sum();
        return count > 0;
    }

    /**
     * 删除收藏夹视频关系
     *
     * @param collectionVideo
     * @return
     */
    @Override
    public Boolean delete(CollectionVideo collectionVideo) {
        return collectionVideoMapper.delete(
                new LambdaQueryWrapper<CollectionVideo>()
                        .eq(CollectionVideo::getCollectionId, collectionVideo.getCollectionId())
                        .eq(CollectionVideo::getVid, collectionVideo.getVid())
        ) == 1;
    }

    /**
     * 删除收藏夹视频关系
     *
     * @param collectionVideoList
     * @return
     */
    @Override
    public Boolean delete(List<CollectionVideo> collectionVideoList) {
        if (ListUtil.isEmpty(collectionVideoList)) {
            log.info("No data to insert, returning true.");
            return true;
        }
        collectionVideoMapper.deleteBatch(collectionVideoList);
        return true;
    }

    /**
     * 删除收藏夹所有视频关系
     *
     * @param collectionId
     * @return
     */
    @Override
    public Boolean deleteByCollectionId(Integer collectionId) {
        collectionVideoMapper.delete(
                new LambdaQueryWrapper<CollectionVideo>()
                        .eq(CollectionVideo::getCollectionId, collectionId)
        );
        return true;
    }

    /**
     * 查询收藏夹视频关系是否存在
     *
     * @param collectionVideo
     * @return
     */
    @Override
    public Boolean exists(CollectionVideo collectionVideo) {
        return collectionVideoMapper.exists(
                new LambdaQueryWrapper<CollectionVideo>()
                        .eq(CollectionVideo::getCollectionId, collectionVideo.getCollectionId())
                        .eq(CollectionVideo::getVid, collectionVideo.getVid())
        );
    }

    /**
     * 分页查询收藏夹视频
     *
     * @param collectionId
     * @param pageNo
     * @param pageSize
     * @return
     */
    @Override
    public Page<CollectionVideo> queryByCollectionId(Integer collectionId, Long pageNo, Long pageSize) {
        Page<CollectionVideo> page = new Page<>(pageNo, pageSize);
        return collectionVideoMapper.selectPage(page,
                new LambdaQueryWrapper<CollectionVideo>()
                        .eq(CollectionVideo::getCollectionId, collectionId)
        );
    }

    /**
     * 批量统计收藏夹视频数量
     *
     * @param collectionIdList
     * @return
     */
    @Override
    public Map<Long, Long> countByCollectionId(List<Integer> collectionIdList) {
        if (ListUtil.isEmpty(collectionIdList)) {
            return Collections.emptyMap();
        }

        QueryWrapper<CollectionVideo> qw = new QueryWrapper<>();
        qw.select("collection_id", "COUNT(*) as count");
        qw.in("collection_id", collectionIdList);
        qw.groupBy("collection_id");
        List<Map<String, Object>> resMap = collectionVideoMapper.selectMaps(qw);

        Map<Long, Long> countMap = new HashMap<>();
        resMap.forEach(rowMap ->
                countMap.put(
                        ((Long) rowMap.get("collection_id")),
                        ((Long) rowMap.get("count"))
                )
        );

        return countMap;
    }

}
