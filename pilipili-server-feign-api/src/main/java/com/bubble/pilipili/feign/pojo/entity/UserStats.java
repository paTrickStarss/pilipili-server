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
 * @date 2025.03.19 22:59
 */
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("user_stats")
public class UserStats extends StatsEntity {

    @TableId
    private Integer uid;
    /**
     * 关注数
     */
    private Long followerCount;
    /**
     * 粉丝数
     */
    private Long fansCount;
    /**
     * 动态数
     */
    private Long dynamicCount;
}
