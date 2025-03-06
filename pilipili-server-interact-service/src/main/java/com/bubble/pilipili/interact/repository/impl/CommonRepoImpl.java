/*
 * Copyright (c) 2025. Bubble
 */

package com.bubble.pilipili.interact.repository.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.bubble.pilipili.common.exception.RepositoryException;
import com.bubble.pilipili.common.util.ListUtil;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

/**
 * @author Bubble
 * @date 2025.03.05 21:42
 */
public class CommonRepoImpl {

    public static <T> Boolean save(
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

        boolean exists = mapper.exists(
                new LambdaQueryWrapper<T>()
                        .eq(targetIdFunc, targetId)
                        .eq(uidFunc, uid)
        );
        if (exists) {
            // 更新
            LambdaUpdateWrapper<T> updateWrapper = new LambdaUpdateWrapper<>();
            updateWrapper.eq(targetIdFunc, targetId);
            updateWrapper.eq(uidFunc, uid);

            updateConsumer.accept(updateWrapper, entity);

            int update = mapper.update(updateWrapper);
            if (update > 1) {
                throw new RepositoryException("更新数量大于1");
            }
            return update == 1;
        } else {
            // 新增
            try {
                return mapper.insert(entity) == 1;
            } catch (DataIntegrityViolationException ex) {
                throw new RepositoryException("新增记录异常");
            }
        }
    }

    public static <T, R> List<R> getStatsBatch(
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
}
