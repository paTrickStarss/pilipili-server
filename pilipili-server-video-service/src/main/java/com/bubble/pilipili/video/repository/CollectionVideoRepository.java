/*
 * Copyright (c) 2025. Bubble
 */

package com.bubble.pilipili.video.repository;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bubble.pilipili.video.pojo.entity.CollectionVideo;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @author Bubble
 * @date 2025.03.17 15:57
 */
@Repository
public interface CollectionVideoRepository {

    /**
     * 保存收藏夹视频关系
     * @param collectionVideo
     * @return
     */
    Boolean save(CollectionVideo collectionVideo);
    /**
     * 保存收藏夹视频关系
     * @param collectionVideoList
     * @return
     */
    Boolean save(List<CollectionVideo> collectionVideoList);

    /**
     * 删除收藏夹视频关系
     * @param collectionVideo
     * @return
     */
    Boolean delete(CollectionVideo collectionVideo);
    /**
     * 删除收藏夹视频关系
     * @param collectionVideoList
     * @return
     */
    Boolean delete(List<CollectionVideo> collectionVideoList);

    /**
     * 删除收藏夹所有视频关系
     * @param collectionId
     * @return
     */
    Boolean deleteByCollectionId(Integer collectionId);

    /**
     * 查询收藏夹视频关系是否存在
     * @param collectionVideo
     * @return
     */
    Boolean exists(CollectionVideo collectionVideo);

    /**
     * 分页查询收藏夹视频
     * @param collectionId
     * @param pageNo
     * @param pageSize
     * @return
     */
    Page<CollectionVideo> queryByCollectionId(Integer collectionId, Long pageNo, Long pageSize);

    /**
     * 批量统计收藏夹视频数量
     * @param collectionIdList
     * @return
     */
    Map<Long, Long> countByCollectionId(List<Integer> collectionIdList);

}
