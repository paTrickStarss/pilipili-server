/*
 * Copyright (c) 2025. Bubble
 */

package com.bubble.pilipili.interact.pojo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 查询用户评论互动状态返回数据
 * @author Bubble
 * @date 2025.03.29 22:31
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class QueryUserCommentDTO implements Serializable {

    private Integer cid;
    private Integer uid;
    private Integer favor;
    private Integer dew;
}
