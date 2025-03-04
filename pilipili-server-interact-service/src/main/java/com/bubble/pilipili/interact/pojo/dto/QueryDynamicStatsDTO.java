/*
 * Copyright (c) 2025. Bubble
 */

package com.bubble.pilipili.interact.pojo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 查询动态统计数据对象
 * @author Bubble
 * @date 2025.03.04 11:58
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class QueryDynamicStatsDTO implements Serializable {

//    private Integer uid;
    /**
     * 动态ID
     */
    private Integer did;
    /**
     * 点赞数
     */
    private Integer favorCount;
    /**
     * 转发数
     */
    private Integer repostCount;
}
