/*
 * Copyright (c) 2025. Bubble
 */

package com.bubble.pilipili.interact.pojo.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @author Bubble
 * @date 2025.03.01 15:00
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("user_dynamic")
public class UserDynamic {

    private Integer uid;
    private Integer did;
    private Integer favor;
    private Integer repost;
    private LocalDateTime updateTime;
}
