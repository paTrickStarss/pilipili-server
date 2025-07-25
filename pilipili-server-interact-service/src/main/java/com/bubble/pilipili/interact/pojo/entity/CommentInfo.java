/*
 * Copyright (c) 2025. Bubble
 */

package com.bubble.pilipili.interact.pojo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 评论信息实体类
 * @author Bubble
 * @date 2025.02.28 21:32
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("comment_info")
public class CommentInfo {

    /**
     * 评论ID
     */
    @TableId(type = IdType.AUTO)
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
     * 删除
     */
    private Integer rm;
}
