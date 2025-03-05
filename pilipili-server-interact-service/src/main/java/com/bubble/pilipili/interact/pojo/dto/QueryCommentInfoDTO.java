/*
 * Copyright (c) 2025. Bubble
 */

package com.bubble.pilipili.interact.pojo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author Bubble
 * @date 2025.03.05 16:43
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class QueryCommentInfoDTO implements Serializable {

    /**
     * 评论ID
     */
    private Integer cid;
    /**
     * 发送用户ID
     */
    private Integer uid;
    /**
     * 回复评论ID
     */
    private Integer parentId;
    /**
     * 回复评论根ID
     */
    private Integer parentRootId;
    /**
     * 评论对象类型 1视频 2动态
     */
    private Integer relaType;
    /**
     * 评论对象ID
     */
    private Integer relaId;
    /**
     * 评论内容
     */
    private String content;
    /**
     * 创建时间
     */
    private LocalDateTime createTime;


    /**
     * 回复数量（针对根评论 即parentId为空的评论 统计其回复数量）
     */
    private Long replyCount;

    /**
     * 点赞数
     */
    private Integer favorCount;
    /**
     * 点踩数
     */
    private Integer dewCount;
}
