/*
 * Copyright (c) 2025. Bubble
 */

package com.bubble.pilipili.feign.pojo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Bubble
 * @date 2025.03.27 12:59
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OssTempSignDTO implements Serializable {

    /**
     * objectName -> accessUrl
     */
    Map<String, String> urlMap = new HashMap<>();
//    /**
//     * 对象名
//     */
//    private String objectName;
//    /**
//     * 临时访问链接
//     */
//    private String accessUrl;
//    /**
//     * 过期时间点
//     */
//    private Date expireAtTime;

    public static OssTempSignDTO createDTO(Map<String, String> urlMap) {
        OssTempSignDTO ossTempSignDTO = new OssTempSignDTO();
        ossTempSignDTO.setUrlMap(urlMap);
        return ossTempSignDTO;
    }

    public static OssTempSignDTO emptyDTO() {
        return createDTO(Collections.emptyMap());
    }
}
