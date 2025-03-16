/*
 * Copyright (c) 2025. Bubble
 */

package com.bubble.pilipili.common.service;

import com.bubble.pilipili.common.pojo.InteractEntity;
import com.bubble.pilipili.common.repository.InteractEntityRepository;
import org.springframework.stereotype.Component;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * @author Bubble
 * @date 2025.03.13 15:47
 */
@Component
public class InteractStatsAction {

    /**
     * 更新互动数据
     * @param interactClz 互动实体类
     * @param id 互动对象ID
     * @param uid 用户ID
     * @param interactEntityRepository 互动对象Repository实例
     * @param userInteractConsumer 互动对象初始化操作
     * @return 是否更新成功
     * @param <T> 互动实体类
     * @throws InstantiationException
     * @throws IllegalAccessException
     */
    public <T extends InteractEntity> Boolean updateInteract(
            Class<T> interactClz,
            Integer id, Integer uid,
            BiConsumer<T, Integer> InteractIdSetter,
            InteractEntityRepository<T> interactEntityRepository,
            Consumer<T> userInteractConsumer
    ) throws InstantiationException, IllegalAccessException {
        T t = generateInteractEntity(interactClz, id, uid, InteractIdSetter, userInteractConsumer);
        return interactEntityRepository.saveInteract(t);
    }

    /**
     * 生成互动关系实体类
     * @param clz
     * @param id
     * @param uid
     * @param consumer
     * @return
     */
    private <T extends InteractEntity> T generateInteractEntity(
            Class<T> clz,
            Integer id, Integer uid,
            BiConsumer<T, Integer> idConsumer,
            Consumer<T> consumer
    ) throws InstantiationException, IllegalAccessException {
//        T entity = clz.getConstructor().newInstance();  // 可访问私有带参构造函数 JDK9以上推荐
        T entity = clz.newInstance();  // 只能访问公有无参构造函数  JDK9以上弃用
        idConsumer.accept(entity, id);
        entity.setUid(uid);
        consumer.accept(entity);
        return entity;
    }

}
