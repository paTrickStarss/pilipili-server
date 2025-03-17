/*
 * Copyright (c) 2025. Bubble
 */

package com.bubble.pilipili.video.controller;

import com.bubble.pilipili.common.http.Controller;
import com.bubble.pilipili.common.http.PageResponse;
import com.bubble.pilipili.common.http.SimpleResponse;
import com.bubble.pilipili.common.pojo.PageDTO;
import com.bubble.pilipili.video.pojo.dto.QueryCollectionInfoDTO;
import com.bubble.pilipili.video.pojo.dto.QueryVideoInfoDTO;
import com.bubble.pilipili.video.pojo.req.*;
import com.bubble.pilipili.video.service.CollectionInfoService;
import com.bubble.pilipili.video.service.CollectionVideoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * @author Bubble
 * @date 2025.03.17 18:14
 */
@RestController
@RequestMapping("/api/video/collection")
@Tag(name = "CollectionController", description = "收藏夹管理相关接口")
public class CollectionController implements Controller {

    @Autowired
    private CollectionInfoService collectionInfoService;
    @Autowired
    private CollectionVideoService collectionVideoService;

    /**
     * 新增收藏夹
     * @param req
     * @return
     */
    @Operation(summary = "新增收藏夹")
    @PostMapping("/save")
    public SimpleResponse<String> save(@Valid @RequestBody SaveCollectionInfoReq req) {
        Boolean b = collectionInfoService.saveCollectionInfo(req);
        return SimpleResponse.result(b);
    }

    /**
     * 更新收藏夹信息
     * @param req
     * @return
     */
    @Operation(summary = "更新收藏夹信息")
    @PutMapping("/update")
    public SimpleResponse<String> update(@Valid @RequestBody UpdateCollectionInfoReq req) {
        Boolean b = collectionInfoService.updateCollectionInfo(req);
        return SimpleResponse.result(b);
    }

    /**
     * 删除收藏夹
     * todo: 权限认证，需要创建者本人或管理员才能执行删除
     * @param collectionId 收藏夹ID
     * @return
     */
    @Operation(summary = "删除收藏夹")
    @DeleteMapping("/{id}")
    public SimpleResponse<String> delete(
            @PathVariable(name = "id")
            @Parameter(name = "collectionId", description = "收藏夹ID")
            Integer collectionId
    ) {
        Boolean b = collectionInfoService.deleteCollectionInfo(collectionId);
        return SimpleResponse.result(b);
    }

    /**
     * 查询收藏夹
     * @param collectionId
     * @return
     */
    @Operation(summary = "查询收藏夹")
    @GetMapping("/{id}")
    public SimpleResponse<QueryCollectionInfoDTO> getCollectionInfo(
            @PathVariable(name = "id")
            @Parameter(name = "collectionId", description = "收藏夹ID")
            Integer collectionId
    ) {
        QueryCollectionInfoDTO dto = collectionInfoService.getCollectionInfo(collectionId);
        return SimpleResponse.success(dto);
    }

    /**
     * 分页查询用户收藏夹信息
     * @param req
     * @return
     */
    @Operation(summary = "分页查询用户收藏夹信息")
    @GetMapping("/query")
    public PageResponse<QueryCollectionInfoDTO> queryCollectionInfo(
            @Valid @ModelAttribute PageQueryCollectionInfoReq req
    ) {
        PageDTO<QueryCollectionInfoDTO> dto = collectionInfoService.queryCollectionInfoByUid(req);
        return PageResponse.success(dto);
    }

    /**
     * 批量保存收藏夹视频关系
     * @param req
     * @return
     */
    @Operation(summary = "批量保存收藏夹视频关系")
    @PostMapping("/saveVideo")
    public SimpleResponse<String> saveCollectionVideo(
            @Valid @RequestBody ChangeCollectionVideoReq req
    ) {
        Boolean b = collectionVideoService.saveCollectionVideo(req);
        return SimpleResponse.result(b);
    }

    /**
     * 批量删除收藏夹视频关系
     * @param req
     * @return
     */
    @Operation(summary = "批量删除收藏夹视频关系")
    @DeleteMapping("/deleteVideo")
    public SimpleResponse<String> deleteCollectionVideo(
            @Valid @ModelAttribute ChangeCollectionVideoReq req
    ) {
        Boolean b = collectionVideoService.deleteCollectionVideo(req);
        return SimpleResponse.result(b);
    }

    /**
     * 分页查询收藏夹视频
     * @param req
     * @return
     */
    @Operation(summary = "分页查询收藏夹视频")
    @GetMapping("/queryVideo")
    public PageResponse<QueryVideoInfoDTO> queryCollectionVideo(
            @Valid @ModelAttribute PageQueryCollectionVideoReq req
    ) {
        PageDTO<QueryVideoInfoDTO> dto = collectionVideoService.queryCollectionVideo(req);
        return PageResponse.success(dto);
    }




}
