/*
 * Copyright (c) 2025. Bubble
 */

package com.bubble.pilipili.feign.api;

import com.bubble.pilipili.common.http.SimpleResponse;
import com.bubble.pilipili.feign.pojo.dto.OssTempSignDTO;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * @author Bubble
 * @date 2025.03.27 13:21
 */
@FeignClient("oss-service")
public interface OssFeignAPI {

    /**
     * 临时签名访问
     * @param objectNameList 待签名对象名列表
     * @return
     */
    @Operation(summary = "临时签名访问")
    @PostMapping("/api/oss/tempSign")
    SimpleResponse<OssTempSignDTO> getTempSigns(@RequestBody List<String> objectNameList);
}
