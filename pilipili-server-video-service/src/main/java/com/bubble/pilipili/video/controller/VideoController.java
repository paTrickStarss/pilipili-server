/*
 * Copyright (c) 2024. Bubble
 */

package com.bubble.pilipili.video.controller;

import com.bubble.pilipili.common.http.Controller;
import com.bubble.pilipili.common.http.PageQueryResponse;
import com.bubble.pilipili.common.http.SimpleResponse;
import com.bubble.pilipili.common.pojo.PageDTO;
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
     * 分页查询用户视频信息
     * @param req
     * @return
     */
    @Operation(summary = "分页查询用户视频信息")
    @GetMapping("/pageQueryByUid")
    public PageQueryResponse<QueryVideoInfoDTO> pageQueryByUid(
            @Valid @ModelAttribute PageQueryVideoInfoReq req
    ) {
        PageDTO<QueryVideoInfoDTO> dto = videoInfoService.pageQueryVideoInfoByUid(req);
        return PageQueryResponse.success(dto);
    }

    /**
     * 分页查询视频信息
     * @param req
     * @return
     */
    @Operation(summary = "分页查询视频信息")
    @GetMapping("/pageQuery")
    public PageQueryResponse<QueryVideoInfoDTO> pageQuery(
            @Valid @ModelAttribute PageQueryVideoInfoReq req
    ) {
        PageDTO<QueryVideoInfoDTO> dto = videoInfoService.pageQueryVideoInfo(req);
        return PageQueryResponse.success(dto);
    }
}
