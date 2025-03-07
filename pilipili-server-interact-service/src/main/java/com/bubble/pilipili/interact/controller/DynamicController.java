/*
 * Copyright (c) 2025. Bubble
 */

package com.bubble.pilipili.interact.controller;

import com.bubble.pilipili.common.http.Controller;
import com.bubble.pilipili.common.http.PageResponse;
import com.bubble.pilipili.common.http.SimpleResponse;
import com.bubble.pilipili.common.pojo.PageDTO;
import com.bubble.pilipili.interact.pojo.dto.QueryDynamicInfoDTO;
import com.bubble.pilipili.interact.pojo.req.PageQueryDynamicInfoReq;
import com.bubble.pilipili.interact.pojo.req.SaveDynamicInfoReq;
import com.bubble.pilipili.interact.service.DynamicInfoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * 动态管理控制器
 * @author Bubble
 * @date 2025.03.01 21:32
 */
@RestController
@RequestMapping("/api/interact/dynamic")
@Tag(name = "DynamicController", description = "动态管理相关接口")
public class DynamicController implements Controller {

    @Autowired
    private DynamicInfoService dynamicInfoService;

    /**
     * 保存动态
     * @param req
     * @return
     */
    @Operation(summary = "保存动态")
    @PostMapping("/save")
    public SimpleResponse<String> save(@Valid @RequestBody SaveDynamicInfoReq req) {
        Boolean b = dynamicInfoService.saveDynamicInfo(req);
        return SimpleResponse.result(b);
    }

    /**
     * 更新动态
     * @param req
     * @return
     */
    @Operation(summary = "更新动态")
    @PutMapping("/update")
    public SimpleResponse<String> update(@Valid @RequestBody SaveDynamicInfoReq req) {
        Boolean b = dynamicInfoService.updateDynamicInfo(req);
        return SimpleResponse.result(b);
    }

    // todo: 关于互动关系的变更请求考虑使用消息队列
    /**
     * 点赞动态
     * @param did
     * @return
     */
    @Operation(summary = "点赞动态")
    @PatchMapping("/favor")
    public SimpleResponse<String> favor(
            @Valid @RequestParam Integer did,
            @Valid @RequestParam Integer uid
    ) {
        Boolean b = dynamicInfoService.favorDynamicInfo(did, uid);
        return SimpleResponse.result(b);
    }

    /**
     * 取消点赞动态
     * @param did
     * @return
     */
    @Operation(summary = "取消点赞动态")
    @PatchMapping("/favorRevoke")
    public SimpleResponse<String> favorRevoke(
            @Valid @RequestParam Integer did,
            @Valid @RequestParam Integer uid
    ) {
        Boolean b = dynamicInfoService.revokeFavorDynamicInfo(did, uid);
        return SimpleResponse.result(b);
    }

    /**
     * 转发动态
     * @param did
     * @return
     */
    @Operation(summary = "转发动态")
    @PatchMapping("/repost")
    public SimpleResponse<String> repost(
            @Valid @RequestParam Integer did,
            @Valid @RequestParam Integer uid
    ) {
        Boolean b = dynamicInfoService.repostDynamicInfo(did, uid);
        return SimpleResponse.result(b);
    }

    /**
     * 删除动态
     * @param did
     * @return
     */
    @Operation(summary = "删除动态")
    @DeleteMapping("/{did}")
    public SimpleResponse<String> remove(@Valid @PathVariable Integer did) {
        Boolean b = dynamicInfoService.deleteDynamicInfo(did);
        return SimpleResponse.result(b);
    }

    /**
     * 分页查询某用户动态
     * @param req
     * @return
     */
    @Operation(summary = "分页查询某用户动态")
    @GetMapping("/pageQueryByUid")
    public PageResponse<QueryDynamicInfoDTO> pageQueryDynamicInfo(
            @Valid @ModelAttribute PageQueryDynamicInfoReq req
    ) {
        PageDTO<QueryDynamicInfoDTO> dto = dynamicInfoService.pageQueryDynamicInfoByUid(req);
        return PageResponse.success(dto);
    }

    /**
     * 查询某条动态
     * @param did
     * @return
     */
    @Operation(summary = "查询某条动态")
    @GetMapping("/{did}")
    public SimpleResponse<QueryDynamicInfoDTO> queryDynamicInfo(
            @Valid @PathVariable Integer did
    ) {
        QueryDynamicInfoDTO dto = dynamicInfoService.queryDynamicInfoDTO(did);
        return SimpleResponse.success(dto);
    }


}
