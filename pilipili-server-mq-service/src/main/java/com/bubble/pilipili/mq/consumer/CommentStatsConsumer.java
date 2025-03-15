/*
 * Copyright (c) 2025. Bubble
 */

package com.bubble.pilipili.mq.consumer;

import com.bubble.pilipili.mq.entity.CommentStatsMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

/**
 * @author Bubble
 * @date 2025.03.14 19:17
 */
@Slf4j
@Service
@RabbitListener(queues = "queue.pilipili.stats.comment")
public class CommentStatsConsumer extends BaseStatsConsumer<CommentStatsMessage>{

    /**
     * 目标消息类
     *
     * @return
     */
    @Override
    protected Class<CommentStatsMessage> getBodyClz() {
        return CommentStatsMessage.class;
    }

    /**
     * 处理消息
     *
     * @param statsBatchList
     */
    @Override
    protected void handleStatsMessages(List<CommentStatsMessage> statsBatchList) {
//        log.debug("statsBatchList size:{}", statsBatchList.size());
        Map<Integer, CommentStatsMessage> statsResultMap = new HashMap<>();
        Map<Integer, List<CommentStatsMessage>> collect = statsBatchList.stream()
                .collect(Collectors.groupingBy(CommentStatsMessage::getCid));

        // 聚合统计
        collect.forEach((key, value) -> {
                    AtomicLong favorCount = new AtomicLong(0);
                    AtomicLong dewCount = new AtomicLong(0);
                    value.forEach(stats -> {
                        favorCount.addAndGet(stats.getFavorCount());
                        dewCount.addAndGet(stats.getDewCount());
                    });
                    statsResultMap.put(key, new CommentStatsMessage(key, favorCount.get(), dewCount.get()));
                });

        List<CommentStatsMessage> statsList = new ArrayList<>(statsResultMap.values());

        statsList.forEach(System.out::println);

        // todo: 执行统计数据更新逻辑

    }


}
