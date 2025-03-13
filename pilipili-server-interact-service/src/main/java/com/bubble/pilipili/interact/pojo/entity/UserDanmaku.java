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
 * 用户弹幕互动实体类
 * @author Bubble
 * @date 2025.03.06 14:26
 */
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("user_danmaku")
public class UserDanmaku extends InteractEntity {

//    @TableField("danmakuId")
    private Integer danmakuId;
    private Integer favor;
    private Integer dew;
}
