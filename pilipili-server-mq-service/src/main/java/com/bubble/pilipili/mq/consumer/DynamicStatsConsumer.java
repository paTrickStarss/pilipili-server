/*
 * Copyright (c) 2025. Bubble
 */

package com.bubble.pilipili.mq.consumer;

import com.bubble.pilipili.mq.entity.DynamicStatsMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Bubble
 * @date 2025.03.15 19:37
 */
@Slf4j
@Service
@RabbitListener(queues = "queue.pilipili.stats.dynamic")
public class DynamicStatsConsumer extends BaseStatsConsumer<DynamicStatsMessage> {
    /**
     * 目标消息类
     *
     * @return
     */
    @Override
    protected Class<DynamicStatsMessage> getBodyClz() {
        return DynamicStatsMessage.class;
    }

    /**
     * 处理消息
     *
     * @param statsBatchList
     */
    @Override
    protected void handleStatsMessages(List<DynamicStatsMessage> statsBatchList) {

    }
}
