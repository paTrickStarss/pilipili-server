/*
 * Copyright (c) 2025. Bubble
 */

package com.bubble.pilipili.common.pojo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.Version;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 统计数据实体类公共父类
 * @author Bubble
 * @date 2025.03.09 22:19
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class StatsEntity {

//    private Integer id;
    private LocalDateTime updateTime;

    @Version
    @TableField("version")
    private Integer version;
}
