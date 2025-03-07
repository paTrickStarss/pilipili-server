/*
 * Copyright (c) 2025. Bubble
 */

package com.bubble.pilipili.user.pojo.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 用户关系
 * @author Bubble
 * @date 2025.03.07 14:34
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("user_rela")
public class UserRela {

    /**
     * 发起关注者uid
     */
    private Integer fromUid;
    /**
     * 被关注者uid
     */
    private Integer toUid;
    /**
     * 特别关注
     */
    private Integer special;
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}
