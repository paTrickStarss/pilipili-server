/*
 * Copyright (c) 2024-2025. Bubble
 */

package com.bubble.pilipili.video.controller;

import com.bubble.pilipili.common.http.Controller;
import com.bubble.pilipili.common.http.PageResponse;
import com.bubble.pilipili.common.http.SimpleResponse;
import com.bubble.pilipili.common.pojo.PageDTO;
import com.bubble.pilipili.video.pojo.dto.QueryCategoryDTO;
import com.bubble.pilipili.video.pojo.dto.QueryVideoInfoDTO;
import com.bubble.pilipili.video.pojo.req.CreateVideoInfoReq;
import com.bubble.pilipili.video.pojo.req.PageQueryVideoInfoReq;
import com.bubble.pilipili.video.pojo.req.UpdateVideoInfoReq;
import com.bubble.pilipili.video.service.VideoInfoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * 视频管理控制器
 * @author liweixin@hcrc1.wecom.work
 * @date 2024/10/23
 */
@RestController
@RequestMapping("/api/video")
@Tag(name = "VideoController", description = "视频管理相关接口")
public class VideoController implements Controller {

    @Autowired
    private VideoInfoService videoInfoService;

    /**
     * 保存视频信息
     * @param req
     * @return
     */
    @Operation(summary = "保存视频信息")
    @PostMapping("/save")
    public SimpleResponse<String> save(@Valid @RequestBody CreateVideoInfoReq req) {
        Boolean b = videoInfoService.saveVideoInfo(req);
        return SimpleResponse.result(b);
    }

    /**
     * 更新视频信息
     * @param req
     * @return
     */
    @Operation(summary = "更新视频信息")
    @PutMapping("/update")
    public SimpleResponse<String> update(@Valid @RequestBody UpdateVideoInfoReq req) {
        Boolean b = videoInfoService.updateVideoInfo(req);
        return SimpleResponse.result(b);
    }

    /**
     * 点赞视频
     * @param vid
     * @param uid
     * @return
     */
    @Operation(summary = "点赞视频")
    @PatchMapping("/favor")
    public SimpleResponse<String> favor(
            @NotBlank @RequestParam Integer vid,
            @NotBlank @RequestParam Integer uid
    ) {
        Boolean b = videoInfoService.favorVideoInfo(vid, uid);
        return SimpleResponse.result(b);
    }

    /**
     * 取消点赞视频
     * @param vid
     * @param uid
     * @return
     */
    @Operation(summary = "取消点赞视频")
    @PatchMapping("/favorRevoke")
    public SimpleResponse<String> favorRevoke(
            @NotBlank @RequestParam Integer vid,
            @NotBlank @RequestParam Integer uid
    ) {
        Boolean b = videoInfoService.revokeFavorVideoInfo(vid, uid);
        return SimpleResponse.result(b);
    }

    /**
     * 投币视频
     * @param vid
     * @param uid
     * @return
     */
    @Operation(summary = "投币视频")
    @PatchMapping("/coin")
    public SimpleResponse<String> coin(
            @NotBlank @RequestParam Integer vid,
            @NotBlank @RequestParam Integer uid
    ) {
        Boolean b = videoInfoService.coinVideoInfo(vid, uid);
        return SimpleResponse.result(b);
    }

    /**
     * 收藏视频
     * @param vid
     * @param uid
     * @return
     */
    @Operation(summary = "收藏视频")
    @PatchMapping("/collect")
    public SimpleResponse<String> collect(
            @NotBlank @RequestParam Integer vid,
            @NotBlank @RequestParam Integer uid
    ) {
        Boolean b = videoInfoService.collectVideoInfo(vid, uid);
        return SimpleResponse.result(b);
    }

    /**
     * 取消收藏视频
     * @param vid
     * @param uid
     * @return
     */
    @Operation(summary = "取消收藏视频")
    @PatchMapping("/collectRevoke")
    public SimpleResponse<String> collectRevoke(
            @NotBlank @RequestParam Integer vid,
            @NotBlank @RequestParam Integer uid
    ) {
        Boolean b = videoInfoService.revokeCollectVideoInfo(vid, uid);
        return SimpleResponse.result(b);
    }

    /**
     * 转发视频
     * @param vid
     * @param uid
     * @return
     */
    @Operation(summary = "转发视频")
    @PatchMapping("/repost")
    public SimpleResponse<String> repost(
            @NotBlank @RequestParam Integer vid,
            @NotBlank @RequestParam Integer uid
    ) {
        Boolean b = videoInfoService.repostVideoInfo(vid, uid);
        return SimpleResponse.result(b);
    }

    /**
     * 取消转发视频
     * @param vid
     * @param uid
     * @return
     */
    @Operation(summary = "取消转发视频")
    @PatchMapping("/repostRevoke")
    public SimpleResponse<String> repostRevoke(
            @NotBlank @RequestParam Integer vid,
            @NotBlank @RequestParam Integer uid
    ) {
        Boolean b = videoInfoService.revokeRepostVideoInfo(vid, uid);
        return SimpleResponse.result(b);
    }

    /**
     * 点踩视频
     * @param vid
     * @param uid
     * @return
     */
    @Operation(summary = "点踩视频")
    @PatchMapping("/dew")
    public SimpleResponse<String> dew(
            @NotBlank @RequestParam Integer vid,
            @NotBlank @RequestParam Integer uid
    ) {
        Boolean b = videoInfoService.dewVideoInfo(vid, uid);
        return SimpleResponse.result(b);
    }

    /**
     * 取消点踩视频
     * @param vid
     * @param uid
     * @return
     */
    @Operation(summary = "取消点踩视频")
    @PatchMapping("/dewRevoke")
    public SimpleResponse<String> dewRevoke(
            @NotBlank @RequestParam Integer vid,
            @NotBlank @RequestParam Integer uid
    ) {
        Boolean b = videoInfoService.revokeDewVideoInfo(vid, uid);
        return SimpleResponse.result(b);
    }

    // TODO: 这个接口路径需要做权限限制（仅限管理员）
    /**
     * 删除视频信息（管理员）
     * @param vid
     * @return
     */
    @Operation(summary = "删除视频信息（管理员）")
    @DeleteMapping("/{vid}")
    public SimpleResponse<String> delete(@Valid @PathVariable Integer vid) {
        Boolean b = videoInfoService.deleteVideoInfo(vid);
        return SimpleResponse.result(b);
    }

    /**
     * 查询视频信息
     * @param vid
     * @return
     */
    @Operation(summary = "查询视频信息")
    @GetMapping("/{vid}")
    public SimpleResponse<QueryVideoInfoDTO> getVideoInfo(@Valid @PathVariable Integer vid) {
        QueryVideoInfoDTO dto = videoInfoService.getVideoInfoById(vid);
        return SimpleResponse.success(dto);
    }

    /**
     * 分页查询用户所有视频信息
     * @param req
     * @return
     */
    @Operation(summary = "分页查询用户所有视频信息")
    @GetMapping("/pageQueryByUid")
    public PageResponse<QueryVideoInfoDTO> pageQueryByUid(
            @Valid @ModelAttribute PageQueryVideoInfoReq req
    ) {
        PageDTO<QueryVideoInfoDTO> dto = videoInfoService.pageQueryVideoInfoByUid(req);
        return PageResponse.success(dto);
    }
    /**
     * 分页查询用户已上架视频信息
     * @param req
     * @return
     */
    @Operation(summary = "分页查询用户已上架视频信息")
    @GetMapping("/pageQueryPassedByUid")
    public PageResponse<QueryVideoInfoDTO> pageQueryPassedByUid(
            @Valid @ModelAttribute PageQueryVideoInfoReq req
    ) {
        PageDTO<QueryVideoInfoDTO> dto = videoInfoService.pageQueryPassedVideoInfoByUid(req);
        return PageResponse.success(dto);
    }

    /**
     * 分页查询视频信息
     * @param req
     * @return
     */
    @Operation(summary = "分页查询视频信息")
    @GetMapping("/pageQuery")
    public PageResponse<QueryVideoInfoDTO> pageQuery(
            @Valid @ModelAttribute PageQueryVideoInfoReq req
    ) {
        PageDTO<QueryVideoInfoDTO> dto = videoInfoService.pageQueryVideoInfo(req);
        return PageResponse.success(dto);
    }

    /**
     * 查询视频分区列表
     * @return
     */
    @Operation(summary = "查询视频分区列表")
    @GetMapping("/categoryList")
    public SimpleResponse<List<QueryCategoryDTO>> queryCategoryList() {
        List<QueryCategoryDTO> dtoList = videoInfoService.queryCategoryList();
        return SimpleResponse.success(dtoList);
    }
}
