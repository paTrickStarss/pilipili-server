/*
 * Copyright (c) 2025. Bubble
 */

package com.bubble.pilipili.common.pojo;

import com.baomidou.mybatisplus.annotation.Version;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 用户与互动对象的公共实体类
 * @author Bubble
 * @date 2025.03.13 16:52
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class InteractEntity {

//    private Integer id;
    private Integer uid;
    private LocalDateTime updateTime;

    @Version
    private Integer version;
}
