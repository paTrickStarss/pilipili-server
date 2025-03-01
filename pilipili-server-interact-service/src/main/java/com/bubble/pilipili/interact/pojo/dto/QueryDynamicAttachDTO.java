/*
 * Copyright (c) 2025. Bubble
 */

package com.bubble.pilipili.interact.pojo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author Bubble
 * @date 2025.03.01 16:26
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class QueryDynamicAttachDTO implements Serializable {

    /**
     * 动态ID
     */
    private Integer did;
    /**
     * 附件UUID
     */
    private String attachUUID;
    /**
     * 附件类型 0图片 1视频
     */
    private Integer attachType;
    /**
     * 附件链接（OSS相对路径）
     */
    private String attachUrl;
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
}
