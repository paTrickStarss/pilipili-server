/*
 * Copyright (c) 2025. Bubble
 */

package com.bubble.pilipili.oss.pojo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author Bubble
 * @date 2025.03.26 18:27
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OssAsyncUploadFileDTO implements Serializable {

    private String taskId;
    private String objectName;
}
