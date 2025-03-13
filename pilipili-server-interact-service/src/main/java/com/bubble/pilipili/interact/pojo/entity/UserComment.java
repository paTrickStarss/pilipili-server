/*
 * Copyright (c) 2025. Bubble
 */

package com.bubble.pilipili.interact.pojo.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.bubble.pilipili.common.pojo.InteractEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 用户评论互动实体类
 * @author Bubble
 * @date 2025.03.05 21:22
 */
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("user_comment")
public class UserComment extends InteractEntity {

//    @TableField("cid")
    private Integer cid;
    private Integer favor;
    private Integer dew;
}
