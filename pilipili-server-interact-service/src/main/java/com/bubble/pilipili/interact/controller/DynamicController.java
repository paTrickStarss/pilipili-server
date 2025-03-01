/*
 * Copyright (c) 2025. Bubble
 */

package com.bubble.pilipili.interact.controller;

import com.bubble.pilipili.common.http.PageQueryResponse;
import com.bubble.pilipili.common.http.SimpleResponse;
import com.bubble.pilipili.common.pojo.PageDTO;
import com.bubble.pilipili.interact.pojo.dto.QueryDynamicInfoDTO;
import com.bubble.pilipili.interact.pojo.req.PageQueryDynamicInfoReq;
import com.bubble.pilipili.interact.pojo.req.SaveDynamicInfoReq;
import com.bubble.pilipili.interact.service.DynamicInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * @author Bubble
 * @date 2025.03.01 21:32
 */
@RestController
@RequestMapping("/api/interact/dynamic")
public class DynamicController {

    @Autowired
    private DynamicInfoService dynamicInfoService;

    @PostMapping("/save")
    public SimpleResponse<String> save(@Valid @RequestBody SaveDynamicInfoReq req) {
        Boolean b = dynamicInfoService.saveDynamicInfo(req);
        return SimpleResponse.success("保存成功");
    }

    @PutMapping("/update")
    public SimpleResponse<String> update(@Valid @RequestBody SaveDynamicInfoReq req) {
        Boolean b = dynamicInfoService.updateDynamicInfo(req);
        return SimpleResponse.success("更新成功");
    }

    @PostMapping("/pageQueryByUid")
    public PageQueryResponse<QueryDynamicInfoDTO> pageQueryDynamicInfo(
            @Valid @RequestBody PageQueryDynamicInfoReq req
    ) {
        PageDTO<QueryDynamicInfoDTO> dto = dynamicInfoService.pageQueryDynamicInfoByUid(req);
        return PageQueryResponse.success(dto);
    }
}
