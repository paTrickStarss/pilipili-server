/*
 * Copyright (c) 2025. Bubble
 */

package com.bubble.pilipili.stats.util;

import com.bubble.pilipili.common.component.RedisHelper;
import com.bubble.pilipili.common.constant.RedisKey;
import com.bubble.pilipili.common.pojo.*;
import com.bubble.pilipili.common.util.ListUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.function.Function;

/**
 * @author Bubble
 * @date 2025.05.05 15:42
 */
@Slf4j
@Component
public class StatsRedisHelper extends RedisHelper {


    /**
     * 执行批量查询统计数据，经过缓存
     * @param idList 待批量查询的id列表
     * @param cacheRootKey 缓存key
     * @param repoStatsMapGetter 数据库批量查询方法
     * @return 统计数据查询结果map
     * @param <S> 统计数据类型
     */
    public <S extends StatsEntity> Map<Integer, S> queryStatsMapViaCache(
            List<Integer> idList,
            RedisKey cacheRootKey,
            Function<List<Integer>, Map<Integer, S>> repoStatsMapGetter,
            Class<S> statsClz
    ) {
        if (ListUtil.isEmpty(idList)) {
            return Collections.emptyMap();
        }

        Map<Integer, S> statsMap = new HashMap<>();
        // 依次遍历每个id，收集缓存未命中的id进行批量数据库查询，最后再缓存起来
        List<Integer> repoQueryIdList = new ArrayList<>();
        for (Integer id : idList) {
            Object cacheStats = getCache(concatKey(cacheRootKey.getKey(), id));
            // 缓存未命中
            if (cacheStats == null) {
                repoQueryIdList.add(id);
            } else {
                // 缓存命中
                if (statsClz.isInstance(cacheStats)) {
                    statsMap.put(id, statsClz.cast(cacheStats));
                }
                // 若缓存值为NullValue对象，则不执行数据库查询
            }
        }

        // 批量查询数据库
        if (!repoQueryIdList.isEmpty()) {
            Map<Integer, S> repoQueryResultMap = repoStatsMapGetter.apply(repoQueryIdList);
            repoQueryIdList.forEach(id -> {
                S repoResultStats = repoQueryResultMap.get(id);
                String cacheKey = concatKey(cacheRootKey.getKey(), id);
                runRLockTask(getRLockName(statsClz, id), () -> {
                    // 即使值为空也进行缓存，防止缓存击穿
                    saveCache(
                            cacheKey,
                            repoResultStats
                    );
                    // 合并最终结果map
                    if (repoResultStats != null) {
                        statsMap.put(id, repoResultStats);
                    }
                }, cacheKey);
            });
        }
        return statsMap;
    }


}
