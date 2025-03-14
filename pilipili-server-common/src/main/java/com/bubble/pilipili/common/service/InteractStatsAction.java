/*
 * Copyright (c) 2025. Bubble
 */

package com.bubble.pilipili.common.service;

import com.bubble.pilipili.common.exception.ServiceOperationException;
import com.bubble.pilipili.common.pojo.InteractEntity;
import com.bubble.pilipili.common.pojo.StatsEntity;
import com.bubble.pilipili.common.repository.InteractEntityRepository;
import com.bubble.pilipili.common.repository.StatsEntityRepository;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * @author Bubble
 * @date 2025.03.13 15:47
 */
public class InteractStatsAction {


    /**
     * 更新互动数据
     * @param interactClz 互动实体类
     * @param statsClz 统计数据实体类
     * @param id 互动对象ID
     * @param uid 用户ID
     * @param interactEntityRepository 互动对象Repository实例
     * @param userInteractConsumer 互动对象初始化操作
     * @param statsEntityRepository 统计数据Repository实例
     * @param statsConsumer 统计数据对象初始化操作
     * @return 是否更新成功
     * @param <T> 互动实体类
     * @param <S> 统计数据实体类
     * @throws InstantiationException
     * @throws IllegalAccessException
     */
    public static <T extends InteractEntity, S extends StatsEntity> Boolean updateInteract(
            Class<T> interactClz, Class<S> statsClz,
            Integer id, Integer uid,
            BiConsumer<T, Integer> InteractIdSetter,
            InteractEntityRepository<T> interactEntityRepository,
            Consumer<T> userInteractConsumer,
            BiConsumer<S, Integer> statsIdSetter,
            StatsEntityRepository<S> statsEntityRepository,
            Consumer<S> statsConsumer
    ) throws InstantiationException, IllegalAccessException {

        Boolean b = interactEntityRepository.saveInteract(
                generateInteractEntity(interactClz, id, uid, InteractIdSetter, userInteractConsumer)
        );
        if (b) {
            //  当新增了一条revoke记录时，若是第一次新增互动关系，则不能执行-1更新（已在service层规避）
            // 更新统计数据
            updateStats(statsClz, id, statsIdSetter, statsConsumer, statsEntityRepository);
        }
        // 没有触发更新，即互动关系已经保存，无需重复更新

        return true;
    }

    /**
     * 生成互动关系实体类
     * @param clz
     * @param id
     * @param uid
     * @param consumer
     * @return
     */
    private static <T extends InteractEntity> T generateInteractEntity(
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

    /**
     * 更新统计数据
     * @param id
     * @todo 统计数据的更新考虑使用消息队列实现
     * @param consumer
     */
    public static <T extends StatsEntity> void updateStats(
            Class<T> clz,
            Integer id,
            BiConsumer<T, Integer> idConsumer,
            Consumer<T> consumer,
            StatsEntityRepository<T> repository
    ) throws InstantiationException, IllegalAccessException {
        T stats = clz.newInstance();
        idConsumer.accept(stats, id);
        consumer.accept(stats);
        Boolean b1 = repository.saveStats(stats);
        if (!b1) {
            throw new ServiceOperationException("动态统计数据保存异常");
        }
    }
}
