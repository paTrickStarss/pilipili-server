/*
 * Copyright (c) 2025. Bubble
 */

package com.bubble.pilipili.common.repository.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.bubble.pilipili.common.exception.RepositoryException;
import com.bubble.pilipili.common.pojo.InteractEntity;
import com.bubble.pilipili.common.pojo.StatsEntity;
import com.bubble.pilipili.common.util.ListUtil;
import com.bubble.pilipili.common.util.MyBatisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DeadlockLoserDataAccessException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 通用Repository逻辑实现
 * @author Bubble
 * @date 2025.03.05 21:42
 */
@Slf4j
@Component
public class CommonRepository {

    /**
     * 保存（新增或更新）复合主键实体类
     * @param entity 实体类实例
     * @param targetIdFunc 主键2
     * @param uidFunc 主键1
     * @param updateConsumer 更新Wrapper逻辑
     * @param mapper Mapper实例
     * @return 是否保存成功
     * @param <T> 实体类
     */
    public <T extends InteractEntity> Boolean saveInteract(
            T entity,
            SFunction<T, Integer> targetIdFunc,
            SFunction<T, Integer> uidFunc,
            BiConsumer<LambdaUpdateWrapper<T>, T> updateConsumer,
            BaseMapper<T> mapper
    ) {
        Integer targetId = targetIdFunc.apply(entity);
        Integer uid = uidFunc.apply(entity);
        if (targetId == null || uid == null) {
            return false;
        }

        LambdaQueryWrapper<T> wrapper = new LambdaQueryWrapper<T>()
                .eq(targetIdFunc, targetId)
                .eq(uidFunc, uid);

        int maxRetries = 3;
        int retryTimes = 0;
        while (retryTimes <= maxRetries) {
            boolean exists = mapper.exists(wrapper);
            T one = mapper.selectOne(wrapper);
            if (one != null) {
                // 更新
//                T t = BaseConverter.getInstance().copyUpdateFieldValue(one, entity);

                LambdaUpdateWrapper<T> updateWrapper = new LambdaUpdateWrapper<>();
                updateWrapper.eq(targetIdFunc, targetId);
                updateWrapper.eq(uidFunc, uid);
//                updateWrapper.eq(T::getVersion, one.getVersion());
//                updateWrapper.setSql("version = version + 1");

                updateConsumer.accept(updateWrapper, entity);

                try {
                    int update = mapper.update(updateWrapper);
                    return update == 1;
//                    if (update == 0) {
//                        retryTimes++;
//                        if (retryTimes <= maxRetries) {
//                            try {
//                                Thread.sleep((long) Math.pow(2, retryTimes) * 50); // 指数退避
//                            } catch (InterruptedException e) {
//                                log.error("InterruptedException:{}", e.getMessage());
//                                Thread.currentThread().interrupt(); // 处理中断状态
//                                throw new RuntimeException(e);
//                            }
//                            log.warn("乐观锁更新重试第{}次", retryTimes);
//                        }
//                    } else if (update == 1) {
//                        return true;
//                    }
                } catch (DeadlockLoserDataAccessException e) {
                    log.error("更新异常: {}", e.getMessage());
                    return false;
                }
            } else {
                // 新增
                try {
                    return mapper.insert(entity) == 1;
                } catch (DuplicateKeyException ex) {
                    log.warn("duplicate key exception:{}", ex.getMessage());
                    // todo: DuplicateKey 重试
                    retryTimes++;
                    if (retryTimes <= maxRetries) {
                        try {
                            Thread.sleep((long) Math.pow(2, retryTimes) * 100); // 指数退避
                        } catch (InterruptedException e) {
                            log.error("InterruptedException:{}", e.getMessage());
                            Thread.currentThread().interrupt(); // 处理中断状态
                            throw new RuntimeException(e);
                        }
                        log.warn("插入重试第{}次", retryTimes);
                    }
                } catch (DataIntegrityViolationException ex) {
                    ex.printStackTrace();
                    throw new RepositoryException("新增记录异常");
                }
            }
        }
        log.warn("重试{}次后，仍无法插入", retryTimes-1);
        return false;
    }

    /**
     * 批量查询统计数据（SQL执行聚合COUNT）
     * @param idList id列表
     * @param resultClz dto类对象
     * @param mapConsumer dto字段填充逻辑
     * @param mapper Mapper实例
     * @param idFieldName 主键id字段名
     * @param fieldNames 聚合目标字段名
     * @return
     * @param <T> 实体类
     * @param <R> DTO类
     */
    public <T, R> List<R> getStatsBatch(
            List<Integer> idList,
            Class<R> resultClz,
            BiConsumer<T, R> mapConsumer,
            BaseMapper<T> mapper,
            String idFieldName,
            String... fieldNames
    ) {
        if (ListUtil.isEmpty(idList)) {
            return Collections.emptyList();
        }

        QueryWrapper<T> queryWrapper = new QueryWrapper<>();
        List<String> selectFields = new ArrayList<>(Collections.singletonList(idFieldName));
        String[] array = Arrays.stream(fieldNames)
                .map(field -> String.join("", "SUM(", field, ") AS ", field))
                .toArray(String[]::new);
        selectFields.addAll(Arrays.asList(array));

        queryWrapper.select(selectFields);
        queryWrapper.in(idFieldName, idList);
        queryWrapper.groupBy(idFieldName);

        return mapper.selectList(queryWrapper).stream()
                .map(entity -> {
                    try {
                        R dto = resultClz.newInstance();
                        mapConsumer.accept(entity, dto);
                        return dto;
                    } catch (InstantiationException | IllegalAccessException e) {
                        throw new RuntimeException(e);
                    }
                })
                .collect(Collectors.toList());
    }

    /**
     * 查询统计数据表
     * @param idList id列表
     * @param idFunc id字段getter函数
     * @param mapper Mapper实例
     * @return Map(id, entity)
     * @param <T> 实体类
     */
    public <T> Map<Integer, T> getStats(
            List<Integer> idList,
            SFunction<T, Integer> idFunc,
            BaseMapper<T> mapper
    ) {
        return mapper.selectList(
                new LambdaQueryWrapper<T>()
                        .in(idFunc, idList)
        ).stream().collect(Collectors.toMap(idFunc, Function.identity()));
    }

    /**
     * 保存（新增或更新）统计数据表
     * @param stats 待保存统计数据实体对象
     * @param mapper Mapper实例
     * @param idFunc 主键getter函数
     * @param countFuncArr 统计字段getter函数
     * @return
     * @param <T>
     */
    @SafeVarargs
    public final <T extends StatsEntity> Boolean saveStats(
            T stats,
            BaseMapper<T> mapper,
            SFunction<T, Integer> idFunc,
            SFunction<T, Long>... countFuncArr
    ) {
        // 乐观锁更新最大允许重试次数：3
        int maxRetryTimes = 3;
        int retryTime = 0;
        Integer id = idFunc.apply(stats);

        while (retryTime <= maxRetryTimes) {
            T one = mapper.selectOne(new LambdaQueryWrapper<T>().eq(idFunc, id));
            if (one == null) {
                // 统计字段值不能小于0
                for (SFunction<T, Long> countFunc : countFuncArr) {
                    Long incrementCount = countFunc.apply(stats);
                    if (incrementCount != null && incrementCount < 0) {
                        // 小于0的字段值需要改为0
                        MyBatisUtil.updateFieldValue(stats, countFunc, 0L);
                    }
                }

                // 新增
                return mapper.insert(stats) == 1;
            } else {
                // 更新
                Integer version = one.getVersion();

                LambdaUpdateWrapper<T> luw = new LambdaUpdateWrapper<>();
                luw.eq(idFunc, id);
                luw.eq(T::getVersion, version);
//                luw.set(T::getVersion, version + 1); // Exception: Could not find lambda cache.
                luw.setSql("version = version + 1");
                for (SFunction<T, Long> countFunc : countFuncArr) {
                    Long incrementCount = countFunc.apply(stats);
                    if (incrementCount != null && incrementCount != 0L) {
                        Long originCount = countFunc.apply(one);
                        long result = originCount + incrementCount;
                        // 小于0的字段值需要改为0
                        luw.set(countFunc, Math.max(result, 0L));
                    }
                }

                int row = mapper.update(luw);
                if (row == 0) {
                    // 乐观锁更新失败，进入重试
                    if (++retryTime <= maxRetryTimes) {
                        log.warn("更新统计数据发生版本冲突，进入第{}次重试", retryTime);
                    }
                } else {
                    // 更新成功
                    break;
                }
            }
        }
        if (retryTime > maxRetryTimes) {
            log.warn("更新统计数据超过最大重试次数{}，更新失败！", maxRetryTimes);
            throw new RepositoryException("统计数据更新失败");
        }

        return true;
    }
}
