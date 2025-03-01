/*
 * Copyright (c) 2025. Bubble
 */

package com.bubble.pilipili.interact.pojo.req;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 保存动态附件信息请求参数
 * @author Bubble
 * @date 2025.03.01 16:17
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SaveDynamicAttachReq implements Serializable {

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
}
