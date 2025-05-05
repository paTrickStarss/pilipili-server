/*
 * Copyright (c) 2025. Bubble
 */

package com.bubble.pilipili.common.pojo;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @author Bubble
 * @date 2025.03.09 17:53
 */
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("comment_stats")
public class CommentStats extends StatsEntity {

    @TableId
    private Integer cid;
    /**
     * 点赞数
     */
    private Long favorCount;
    /**
     * 点踩数
     */
    private Long dewCount;

}
