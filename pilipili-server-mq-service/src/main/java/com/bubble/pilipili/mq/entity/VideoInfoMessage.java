/*
 * Copyright (c) 2025. Bubble
 */

package com.bubble.pilipili.mq.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Bubble
 * @date 2025.03.26 16:08
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class VideoInfoMessage {

    /**
     * 上传任务ID
     */
    private String taskId;
    /**
     * 视频时长（秒）
     */
    private Long duration;
    /**
     * 视频ID
     */
    private Integer vid;
    /**
     * 视频封面路径
     */
    private String coverUrl;
    /**
     * 视频内容路径
     */
    private String contentUrl;

}
