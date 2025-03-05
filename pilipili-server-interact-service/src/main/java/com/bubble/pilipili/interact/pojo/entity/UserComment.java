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
 * @date 2025.03.05 21:22
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("user_comment")
public class UserComment {

    private Integer cid;
    private Integer uid;
    private Integer favor;
    private Integer dew;
    private LocalDateTime updateTime;

    @Version
    private Integer version;
}
