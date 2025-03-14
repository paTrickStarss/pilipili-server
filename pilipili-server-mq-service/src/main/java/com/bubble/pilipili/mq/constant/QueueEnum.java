/*
 * Copyright (c) 2025. Bubble
 */

package com.bubble.pilipili.mq.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * MQ队列枚举
 * @author Bubble
 * @date 2025.03.14 18:19
 */
@Getter
@AllArgsConstructor
public enum QueueEnum {

    QUEUE_STATS_COMMENT("queue.pilipili.stats.comment", "comment", "评论统计数据队列"),
    QUEUE_STATS_DYNAMIC("queue.pilipili.stats.dynamic", "dynamic", "动态统计数据队列"),
    QUEUE_STATS_DANMAKU("queue.pilipili.stats.danmaku", "danmaku", "弹幕统计数据队列"),
    QUEUE_STATS_VIDEO("queue.pilipili.stats.video", "video", "视频统计数据队列"),
    ;

    private final String name;
    private final String routingKey;
    private final String desc;

//    QueueEnum(String name, String routingKey, String desc) {
//        this.name = name;
//        this.routingKey = routingKey;
//        this.desc = desc;
//    }
}
