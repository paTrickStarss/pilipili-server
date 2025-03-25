/*
 * Copyright (c) 2025. Bubble
 */

package com.bubble.pilipili.video.repository.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.bubble.pilipili.video.mapper.PrimaryCategoryMapper;
import com.bubble.pilipili.video.pojo.entity.PrimaryCategory;
import com.bubble.pilipili.video.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Bubble
 * @date 2025.03.25 15:36
 */
@Repository
public class CategoryRepositoryImpl implements CategoryRepository {

    @Autowired
    private PrimaryCategoryMapper primaryCategoryMapper;

    /**
     * @return
     */
    @Override
    public List<PrimaryCategory> queryAllCategory() {
        return primaryCategoryMapper.selectList(
                new LambdaQueryWrapper<PrimaryCategory>()
                        .last("LIMIT 100")
        );
    }
}
