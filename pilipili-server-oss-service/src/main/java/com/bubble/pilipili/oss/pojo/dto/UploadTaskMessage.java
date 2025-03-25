/*
 * Copyright (c) 2025. Bubble
 */

package com.bubble.pilipili.oss.pojo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 上传任务通讯消息实体类
 * @author Bubble
 * @date 2025.03.25 19:56
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UploadTaskMessage implements Serializable {

    /**
     * 上传任务ID
     */
    private String taskId;
    /**
     * 用户名
     */
    private String username;
    /**
     * 任务状态 0已创建未开始 1上传中 2上传完成 3错误
     */
    private Integer status;
    /**
     * 当前上传进度  0~100 %
     */
    private Integer progress;
    /**
     * 备注消息
     */
    private String msg;
    /**
     * 消息发送时间戳
     */
    private Long msgTime;
}
