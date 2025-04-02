/*
 * Copyright (c) 2025. Bubble
 */

package com.bubble.pilipili.feign.pojo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author Bubble
 * @date 2025.03.21 15:48
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OssUploadFileDTO implements Serializable {

    private Boolean success;
    private String objectName;
    private String msg;

    public static OssUploadFileDTO success(String filePath) {
        return new OssUploadFileDTO(true, filePath, null);
    }
    public static OssUploadFileDTO failed(String msg) {
        return new OssUploadFileDTO(false, null, msg);
    }
}
