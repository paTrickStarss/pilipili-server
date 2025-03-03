/*
 * Copyright (c) 2025. Bubble
 */

package com.bubble.pilipili.interact.controller;

import com.bubble.pilipili.common.http.Controller;
import com.bubble.pilipili.common.http.PageQueryResponse;
import com.bubble.pilipili.common.http.SimpleResponse;
import com.bubble.pilipili.common.pojo.PageDTO;
import com.bubble.pilipili.interact.pojo.dto.QueryDynamicInfoDTO;
import com.bubble.pilipili.interact.pojo.req.PageQueryDynamicInfoReq;
import com.bubble.pilipili.interact.pojo.req.QueryDynamicInfoReq;
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
public class DynamicController implements Controller {

    @Autowired
    private DynamicInfoService dynamicInfoService;

    /**
     * 保存动态
     * @param req
     * @return
     */
    @PostMapping("/save")
    public SimpleResponse<String> save(@Valid @RequestBody SaveDynamicInfoReq req) {
        Boolean b = dynamicInfoService.saveDynamicInfo(req);
        if (b) {
            return SimpleResponse.success("保存成功");
        }
        return SimpleResponse.failed("保存失败");
    }

    /**
     * 更新动态
     * @param req
     * @return
     */
    @PutMapping("/update")
    public SimpleResponse<String> update(@Valid @RequestBody SaveDynamicInfoReq req) {
        Boolean b = dynamicInfoService.updateDynamicInfo(req);
        if (b) {
            return SimpleResponse.success("更新成功");
        }
        return SimpleResponse.failed("更新失败");
    }

    /**
     * 删除动态
     * @param req
     * @return
     */
    @DeleteMapping("/remove")
    public SimpleResponse<String> remove(@Valid @ModelAttribute QueryDynamicInfoReq req) {
        Boolean b = dynamicInfoService.deleteDynamicInfo(req.getDid());
        if (b) {
            return SimpleResponse.success("删除成功");
        }
        return SimpleResponse.failed("删除失败");
    }

    /**
     * 分页查询某用户动态
     * @param req
     * @return
     */
    @GetMapping("/pageQueryByUid")
    public PageQueryResponse<QueryDynamicInfoDTO> pageQueryDynamicInfo(
            @Valid @ModelAttribute PageQueryDynamicInfoReq req
    ) {
        PageDTO<QueryDynamicInfoDTO> dto = dynamicInfoService.pageQueryDynamicInfoByUid(req);
        return PageQueryResponse.success(dto);
    }

    /**
     * 查询某条动态
     * @param req
     * @return
     */
    @GetMapping("/query")
    public SimpleResponse<QueryDynamicInfoDTO> queryDynamicInfo(
            @Valid @ModelAttribute QueryDynamicInfoReq req
    ) {
        QueryDynamicInfoDTO dto = dynamicInfoService.queryDynamicInfoDTO(req.getDid());
        if (dto == null) {
            return SimpleResponse.success("数据不存在");
        }
        return SimpleResponse.success(dto);
    }
}
