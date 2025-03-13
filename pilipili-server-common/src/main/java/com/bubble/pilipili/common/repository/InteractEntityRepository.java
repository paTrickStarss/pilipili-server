/*
 * Copyright (c) 2025. Bubble
 */

package com.bubble.pilipili.common.repository;

import com.bubble.pilipili.common.pojo.InteractEntity;
import org.springframework.stereotype.Repository;

/**
 * @author Bubble
 * @date 2025.03.13 17:00
 */
@Repository
public interface InteractEntityRepository<T extends InteractEntity> {

    /**
     * 保存互动数据
     * @param interactEntity
     * @return
     */
    Boolean saveInteract(T interactEntity);

}
