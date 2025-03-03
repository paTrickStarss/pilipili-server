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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * @author liweixin@hcrc1.wecom.work
 * @date 2024/10/23
 */
@RestController
@RequestMapping("/api/video")
public class VideoController implements Controller {

    @Autowired
    private VideoInfoService videoInfoService;

    /**
     * 保存视频信息
     * @param req
     * @return
     */
    @PostMapping("/save")
    public SimpleResponse<String> save(@Valid @RequestBody CreateVideoInfoReq req) {
        Boolean b = videoInfoService.saveVideoInfo(req);
        if (b) {
            return SimpleResponse.success("保存成功！");
        }
        return SimpleResponse.failed("保存失败！");
    }

    /**
     * 更新视频信息
     * @param req
     * @return
     */
    @PatchMapping("/update")
    public SimpleResponse<String> update(@Valid @RequestBody UpdateVideoInfoReq req) {
        Boolean b = videoInfoService.updateVideoInfo(req);
        if (b) {
            return SimpleResponse.success("更新成功！");
        }
        return SimpleResponse.failed("更新失败！");
    }

    // TODO: 这个接口路径需要做权限限制（仅限管理员）
    /**
     * 删除视频信息（管理员）
     * @param vid
     * @return
     */
    @DeleteMapping("/delete")
    public SimpleResponse<String> delete(@Valid @RequestParam Integer vid) {
        Boolean b = videoInfoService.deleteVideoInfo(vid);
        if (b) {
            return SimpleResponse.success("删除成功！");
        }
        return SimpleResponse.failed("删除失败！");
    }

    /**
     * 查询视频信息
     * @param vid
     * @return
     */
    @GetMapping("/{vid}")
    public SimpleResponse<QueryVideoInfoDTO> getVideoInfo(@Valid @PathVariable Integer vid) {
        QueryVideoInfoDTO queryVideoInfoDTO = videoInfoService.getVideoInfoById(vid);
        return SimpleResponse.success(queryVideoInfoDTO);
    }

    /**
     * 分页查询用户视频信息
     * @param req
     * @return
     */
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
    @GetMapping("/pageQuery")
    public PageQueryResponse<QueryVideoInfoDTO> pageQuery(
            @Valid @ModelAttribute PageQueryVideoInfoReq req
    ) {
        PageDTO<QueryVideoInfoDTO> dto = videoInfoService.pageQueryVideoInfo(req);
        return PageQueryResponse.success(dto);
    }
}
