/*
 * Copyright (c) 2025. Bubble
 */

package com.bubble.pilipili.interact.pojo.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.Version;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @author Bubble
 * @date 2025.03.06 14:26
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("user_danmaku")
public class UserDanmaku {

    private Integer danmakuId;
    private Integer uid;
    private Integer favor;
    private Integer dew;
    private LocalDateTime updateTime;

    @Version
    private Integer version;
}
