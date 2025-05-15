/*
 * Copyright (c) 2025. Bubble
 */

package com.bubble.pilipili.feign.pojo.req;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 更新视频信息消息
 * @author Bubble
 * @date 2025.03.26 15:56
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SendVideoInfoReq implements Serializable {

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
//    @NotEmpty(message = "请传入视频ID")
    private Integer vid;
    /**
     * 视频封面路径
     */
    private String coverUrl;
    /**
     * 视频内容路径
     */
    private String contentUrl;
    /**
     * 视频状态：0上传中 1审核中 2审核通过 3审核不通过 4下架
     */
    private Integer status;
}
