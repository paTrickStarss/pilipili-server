/*
 * Copyright (c) 2025. Bubble
 */

package com.bubble.pilipili.video.pojo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @author Bubble
 * @date 2025.03.25 15:27
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("primary_category")
public class PrimaryCategory {

    @TableId(type = IdType.AUTO)
    private Integer primaryCategoryId;
    private String name;
    private String description;

    private LocalDateTime createTime;
    private Integer rm;
}
