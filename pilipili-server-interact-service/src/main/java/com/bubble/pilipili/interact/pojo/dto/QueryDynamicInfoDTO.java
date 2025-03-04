/*
 * Copyright (c) 2025. Bubble
 */

package com.bubble.pilipili.interact.pojo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author Bubble
 * @date 2025.03.01 16:24
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class QueryDynamicInfoDTO implements Serializable {


    /**
     * 动态ID
     */
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
     * 附件
     */
    List<QueryDynamicAttachDTO> attachList;

    /**
     * 点赞数
     */
    private Integer favorCount;
    /**
     * 转发数
     */
    private Integer repostCount;
}
