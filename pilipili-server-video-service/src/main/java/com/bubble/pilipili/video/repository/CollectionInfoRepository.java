/*
 * Copyright (c) 2025. Bubble
 */

package com.bubble.pilipili.video.repository;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bubble.pilipili.video.pojo.entity.CollectionInfo;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Bubble
 * @date 2025.03.17 15:43
 */
@Repository
public interface CollectionInfoRepository {

    /**
     * 保存收藏夹信息
     * @param collectionInfo
     * @return
     */
    Boolean save(CollectionInfo collectionInfo);

    /**
     * 更新收藏夹信息
     * @param collectionInfo
     * @return
     */
    Boolean update(CollectionInfo collectionInfo);

    /**
     * 删除收藏夹
     * @param collectionId
     * @return
     */
    Boolean delete(Integer collectionId);

    /**
     * 查询指定收藏夹信息
     * @param collectionId
     * @return
     */
    CollectionInfo query(Integer collectionId);

    /**
     * 分页查询用户收藏夹
     * @param uid
     * @param pageNo
     * @param pageSize
     * @return
     */
    Page<CollectionInfo> queryByUid(Integer uid, Long pageNo, Long pageSize);
}
