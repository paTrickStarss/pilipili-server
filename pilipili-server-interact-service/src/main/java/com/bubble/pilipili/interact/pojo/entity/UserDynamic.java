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
 * 用户动态互动实体类
 * @author Bubble
 * @date 2025.03.01 15:00
 */
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("user_dynamic")
public class UserDynamic extends InteractEntity {

//    @TableField("did")
    private Integer did;
    private Integer favor;
    private Integer repost;
}
