/*
 * Copyright (c) 2025. Bubble
 */

package com.bubble.pilipili.feign.pojo.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.bubble.pilipili.common.pojo.StatsEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @author Bubble
 * @date 2025.03.09 20:33
 */
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("danmaku_stats")
public class DanmakuStats extends StatsEntity {

    @TableId
    private Integer danmakuId;
    private Long favorCount;
    private Long dewCount;
}
