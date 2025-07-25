/*
 * Copyright (c) 2025. Bubble
 */

package com.bubble.pilipili.mq.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * MQ交换机枚举
 * @author Bubble
 * @date 2025.03.14 18:18
 */
@Getter
@AllArgsConstructor
public enum ExchangeEnum {

    EXCHANGE_STATS("exchange.direct.pilipili.stats", "direct", "统计数据交换机"),
    EXCHANGE_INFO("exchange.direct.pilipili.info", "direct", "实体信息数据交换机"),
    EXCHANGE_DELAY("exchange.direct.pilipili.delay", "x-delayed-message", "延迟消息交换机"),
    ;

    private final String name;
    private final String type;
    private final String desc;

}
