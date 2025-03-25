/*
 * Copyright (c) 2025. Bubble
 */

package com.bubble.pilipili.video.repository;

import com.bubble.pilipili.video.pojo.entity.PrimaryCategory;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Bubble
 * @date 2025.03.25 15:36
 */
@Repository
public interface CategoryRepository {

    List<PrimaryCategory> queryAllCategory();
}
