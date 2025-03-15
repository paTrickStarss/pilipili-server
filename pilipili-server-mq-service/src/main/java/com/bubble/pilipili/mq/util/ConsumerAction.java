/*
 * Copyright (c) 2025. Bubble
 */

package com.bubble.pilipili.mq.util;

import com.bubble.pilipili.common.pojo.StatsEntity;
import com.bubble.pilipili.common.pojo.converter.BaseConverter;
import com.bubble.pilipili.feign.api.StatsFeignAPI;
import com.bubble.pilipili.mq.entity.StatsMessage;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.BinaryOperator;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author Bubble
 * @date 2025.03.15 21:23
 */
@Slf4j
public class ConsumerAction {

    /**
     * 批量消费统计数据信息
     * @param messageClz
     * @param entityClz
     * @param statsBatchList
     * @param idGetter
     * @param zeroInitializer
     * @param reduceMapper
     * @param accumulator
     * @param statsFeignAPI
     * @param <T>
     * @param <E>
     */
    public static <T extends StatsMessage, E extends StatsEntity> void consumeStatsMessage(
            Class<T> messageClz,
            Class<E> entityClz,
            List<T> statsBatchList,
            Function<T, Integer> idGetter,
            Consumer<T> zeroInitializer,
            Function<T, T> reduceMapper,
            BinaryOperator<T> accumulator,
            StatsFeignAPI statsFeignAPI
    ) {
        // 聚合统计
        Map<Integer, T> statsResultMap = statsBatchList.stream()
                .collect(Collectors.groupingBy(
                        idGetter,
                        Collectors.collectingAndThen(
                                Collectors.reducing(
                                        getZeroInstance(messageClz, zeroInitializer),
                                        reduceMapper,
                                        accumulator
                                ),
                                result -> result
                        )
                ));

        List<T> statsList = new ArrayList<>(statsResultMap.values());

        // todo: 执行统计数据更新逻辑
        List<E> statsEntityList =
                BaseConverter.getInstance().copyFieldValueList(statsList, entityClz);
        statsEntityList.forEach(stats -> {
            log.debug("Before saveStats: {}", stats);
//            SimpleResponse<String> response = statsFeignAPI.saveStats(stats);
//            if (!response.isSuccess()) {
//                log.error("saveStats error:{}", response.getMsg());
//            }
        });
    }

    private static <T extends StatsMessage> T getZeroInstance(
            Class<T> messageClz,
            Consumer<T> zeroInitializer
    ) {
        try {
            T message = messageClz.newInstance();
            zeroInitializer.accept(message);
            return message;
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
