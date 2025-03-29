/*
 * Copyright (c) 2025. Bubble
 */

package com.bubble.pilipili.interact.controller;

import com.bubble.pilipili.common.http.Controller;
import com.bubble.pilipili.common.http.PageResponse;
import com.bubble.pilipili.common.http.SimpleResponse;
import com.bubble.pilipili.common.pojo.PageDTO;
import com.bubble.pilipili.interact.pojo.dto.QueryDanmakuInfoDTO;
import com.bubble.pilipili.interact.pojo.dto.QueryUserDanmakuDTO;
import com.bubble.pilipili.interact.pojo.req.PageQueryDanmakuInfoReq;
import com.bubble.pilipili.interact.pojo.req.SaveDanmakuInfoReq;
import com.bubble.pilipili.interact.service.DanmakuInfoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

/**
 * 弹幕管理控制器
 * @author Bubble
 * @date 2025.03.06 15:26
 */
@RestController
@RequestMapping("/api/interact/danmaku")
@Tag(name = "DanmakuController", description = "弹幕管理相关接口")
public class DanmakuController implements Controller {

    @Autowired
    private DanmakuInfoService danmakuInfoService;

    /**
     * 保存弹幕
     * @param req
     * @return
     */
    @Operation(summary = "保存弹幕")
    @PostMapping("/save")
    public SimpleResponse<String> save(@Valid @RequestBody SaveDanmakuInfoReq req) {
        Boolean b = danmakuInfoService.saveDanmakuInfo(req);
        return SimpleResponse.result(b);
    }

    /**
     * 删除弹幕
     * @param danmakuId
     * @return
     */
    @Operation(summary = "删除弹幕")
    @DeleteMapping("{danmakuId}")
    public SimpleResponse<String> delete(@PathVariable Integer danmakuId) {
        Boolean b = danmakuInfoService.deleteDanmakuInfo(danmakuId);
        return SimpleResponse.result(b);
    }

    /**
     * 点赞弹幕
     * @param danmakuId
     * @param uid
     * @return
     */
    @Operation(summary = "点赞弹幕")
    @PatchMapping("/favor")
    public SimpleResponse<String> favor(
            @NotBlank @RequestParam Integer danmakuId,
            @NotBlank @RequestParam Integer uid
    ) {
        Boolean b = danmakuInfoService.favorDanmakuInfo(danmakuId, uid);
        return SimpleResponse.result(b);
    }

    /**
     * 取消点赞弹幕
     * @param danmakuId
     * @param uid
     * @return
     */
    @Operation(summary = "取消点赞弹幕")
    @PatchMapping("/favorRevoke")
    public SimpleResponse<String> favorRevoke(
            @NotBlank @RequestParam Integer danmakuId,
            @NotBlank @RequestParam Integer uid
    ) {
        Boolean b = danmakuInfoService.revokeFavorDanmakuInfo(danmakuId, uid);
        return SimpleResponse.result(b);
    }

    /**
     * 点踩弹幕
     * @param danmakuId
     * @param uid
     * @return
     */
    @Operation(summary = "点踩弹幕")
    @PatchMapping("/dew")
    public SimpleResponse<String> dew(
            @NotBlank @RequestParam Integer danmakuId,
            @NotBlank @RequestParam Integer uid
    ) {
        Boolean b = danmakuInfoService.dewDanmakuInfo(danmakuId, uid);
        return SimpleResponse.result(b);
    }

    /**
     * 取消点踩弹幕
     * @param danmakuId
     * @param uid
     * @return
     */
    @Operation(summary = "取消点踩弹幕")
    @PatchMapping("/dewRevoke")
    public SimpleResponse<String> dewRevoke(
            @NotBlank @RequestParam Integer danmakuId,
            @NotBlank @RequestParam Integer uid
    ) {
        Boolean b = danmakuInfoService.revokeDewDanmakuInfo(danmakuId, uid);
        return SimpleResponse.result(b);
    }

    /**
     * 查询用户弹幕互动状态
     * @param danmakuId
     * @param uid
     * @return
     */
    @Operation(summary = "查询用户弹幕互动状态")
    @GetMapping("/getUserDanmaku")
    public SimpleResponse<QueryUserDanmakuDTO> queryUserDanmaku(
            @NotBlank @RequestParam Integer danmakuId,
            @NotBlank @RequestParam Integer uid
    ) {
        QueryUserDanmakuDTO dto = danmakuInfoService.queryUserDanmaku(danmakuId, uid);
        return SimpleResponse.success(dto);
    }

    /**
     * 查询视频弹幕
     * @param req
     * @return
     */
    @Operation(summary = "查询视频弹幕")
    @GetMapping("/pageQueryByVid")
    public PageResponse<QueryDanmakuInfoDTO> pageQueryByVid(
            @Valid @ModelAttribute PageQueryDanmakuInfoReq req
    ) {
        PageDTO<QueryDanmakuInfoDTO> dto = danmakuInfoService.pageQueryDanmakuInfoByVid(req);
        return PageResponse.success(dto);
    }

    /**
     * 查询用户弹幕
     * @param req
     * @return
     */
    @Operation(summary = "查询用户弹幕")
    @GetMapping("/pageQueryByUid")
    public PageResponse<QueryDanmakuInfoDTO> pageQueryByUid(
            @Valid @ModelAttribute PageQueryDanmakuInfoReq req
    ) {
        PageDTO<QueryDanmakuInfoDTO> dto = danmakuInfoService.pageQueryDanmakuInfoByUid(req);
        return PageResponse.success(dto);
    }

    /**
     * 查询视频用户弹幕
     * @param req
     * @return
     */
    @Operation(summary = "查询视频用户弹幕")
    @GetMapping("/pageQueryByVidAndUid")
    public PageResponse<QueryDanmakuInfoDTO> pageQueryByVidAndUid(
            @Valid @ModelAttribute PageQueryDanmakuInfoReq req
    ) {
        PageDTO<QueryDanmakuInfoDTO> dto = danmakuInfoService.pageQueryDanmakuInfoByVidAndUid(req);
        return PageResponse.success(dto);
    }

}
