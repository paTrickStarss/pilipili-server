/*
 * Copyright (c) 2025. Bubble
 */

package com.bubble.pilipili.interact.pojo.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.bubble.pilipili.common.pojo.StatsEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @author Bubble
 * @date 2025.03.09 20:49
 */
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("dynamic_stats")
public class DynamicStats extends StatsEntity {

    @TableId
    private Integer did;
    private Long favorCount;
    private Long commentCount;
    private Long repostCount;
    private Long dewCount;
}
