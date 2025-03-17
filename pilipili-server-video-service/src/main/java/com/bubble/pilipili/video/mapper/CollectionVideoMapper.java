/*
 * Copyright (c) 2025. Bubble
 */

package com.bubble.pilipili.video.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bubble.pilipili.video.pojo.entity.CollectionVideo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author Bubble
 * @date 2025.03.17 15:42
 */
@Mapper
public interface CollectionVideoMapper extends BaseMapper<CollectionVideo> {

    int deleteBatch(@Param("collectionVideoList") List<CollectionVideo> collectionVideoList);
}
