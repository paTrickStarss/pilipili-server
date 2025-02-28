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
 * @author Bubble
 * @date 2025.02.28 22:03
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("dynamic_info")
public class DynamicInfo {

    /**
     * 动态ID
     */
    @TableId(type = IdType.AUTO)
    private Integer did;
    /**
     * 用户ID
     */
    private Integer uid;
    /**
     * 内容
     */
    private String content;
    /**
     * 是否为转发动态
     */
    private Integer isRepost;
    /**
     * 引用类型 1视频 2动态
     */
    private Integer relaType;
    /**
     * 引用对象ID
     */
    private Integer relaId;
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
    /**
     * 删除
     */
    private Integer rm;
}
