/*
 * Copyright (c) 2025. Bubble
 */

package com.bubble.pilipili.interact.controller;

import com.bubble.pilipili.common.http.Controller;
import com.bubble.pilipili.common.http.PageResponse;
import com.bubble.pilipili.common.http.SimpleResponse;
import com.bubble.pilipili.common.pojo.PageDTO;
import com.bubble.pilipili.interact.pojo.dto.QueryCommentInfoDTO;
import com.bubble.pilipili.interact.pojo.req.PageQueryCommentInfoReq;
import com.bubble.pilipili.interact.pojo.req.SaveCommentInfoReq;
import com.bubble.pilipili.interact.service.CommentInfoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

/**
 * 评论管理控制器
 * @author Bubble
 * @date 2025.03.05 20:30
 */
@RestController
@RequestMapping("/api/interact/comment")
@Tag(name = "CommentController", description = "评论管理相关接口")
public class CommentController implements Controller {

    @Autowired
    private CommentInfoService commentInfoService;

    /**
     * 新增评论
     * @param req
     * @return
     */
    @Operation(summary = "新增评论")
    @PostMapping("/save")
    public SimpleResponse<String> save(@Valid @RequestBody SaveCommentInfoReq req) {
        Boolean b = commentInfoService.saveCommentInfo(req);
        return SimpleResponse.result(b);
    }

    /**
     * 删除评论
     * @param cid
     * @return
     */
    @Operation(summary = "删除评论")
    @DeleteMapping("/{cid}")
    public SimpleResponse<String> delete(
            @NotBlank(message = "请传入cid") @PathVariable Integer cid
    ) {
        Boolean b = commentInfoService.deleteCommentInfo(cid);
        return SimpleResponse.result(b);
    }

    /**
     * 点赞评论
     * @param cid
     * @param uid
     * @return
     */
    @Operation(summary = "点赞评论")
    @PatchMapping("/favor")
    public SimpleResponse<String> favor(
            @NotBlank(message = "请传入cid") @RequestParam Integer cid,
            @NotBlank(message = "请传入uid") @RequestParam Integer uid
    ) {
        Boolean b = commentInfoService.favorCommentInfo(cid, uid);
        return SimpleResponse.result(b);
    }

    /**
     * 取消点赞评论
     * @param cid
     * @param uid
     * @return
     */
    @Operation(summary = "取消点赞评论")
    @PatchMapping("/favorRevoke")
    public SimpleResponse<String> favorRevoke(
            @NotBlank(message = "请传入cid") @RequestParam Integer cid,
            @NotBlank(message = "请传入uid") @RequestParam Integer uid
    ) {
        Boolean b = commentInfoService.revokeFavorCommentInfo(cid, uid);
        return SimpleResponse.result(b);
    }

    /**
     * 点踩评论
     * @param cid
     * @param uid
     * @return
     */
    @Operation(summary = "点踩评论")
    @PatchMapping("/dew")
    public SimpleResponse<String> dew(
            @NotBlank(message = "请传入cid") @RequestParam Integer cid,
            @NotBlank(message = "请传入uid") @RequestParam Integer uid
    ) {
        Boolean b = commentInfoService.dewCommentInfo(cid, uid);
        return SimpleResponse.result(b);
    }

    /**
     * 取消点踩评论
     * @param cid
     * @param uid
     * @return
     */
    @Operation(summary = "取消点踩评论")
    @PatchMapping("/dewRevoke")
    public SimpleResponse<String> dewRevoke(
            @NotBlank(message = "请传入cid") @RequestParam Integer cid,
            @NotBlank(message = "请传入uid") @RequestParam Integer uid
    ) {
        Boolean b = commentInfoService.revokeDewCommentInfo(cid, uid);
        return SimpleResponse.result(b);
    }

    /**
     * 查询指定评论信息
     * @param cid
     * @return
     */
    @Operation(summary = "查询指定评论")
    @GetMapping("{cid}")
    public SimpleResponse<QueryCommentInfoDTO> getCommentInfo(
            @NotBlank(message = "请传入cid") @PathVariable Integer cid
    ) {
        QueryCommentInfoDTO dto = commentInfoService.queryCommentInfo(cid);
        return SimpleResponse.success(dto);
    }

    /**
     * 分页查询某对象的所有评论
     * @param req
     * @return
     */
    @Operation(summary = "分页查询某对象评论")
    @GetMapping("/pageQueryByRela")
    public PageResponse<QueryCommentInfoDTO> pageQueryByRela(
            @Valid @ModelAttribute PageQueryCommentInfoReq req
    ) {
        PageDTO<QueryCommentInfoDTO> dto = commentInfoService.pageQueryCommentInfoByRela(req);
        return PageResponse.success(dto);
    }

    /**
     * 分页查询评论的所有回复
     * @param req
     * @return
     */
    @Operation(summary = "分页查询评论回复")
    @GetMapping("/pageQueryCommentReply")
    public PageResponse<QueryCommentInfoDTO> pageQueryCommentReply(
            @Valid @ModelAttribute PageQueryCommentInfoReq req
    ) {
        PageDTO<QueryCommentInfoDTO> dto = commentInfoService.pageQueryCommentReply(req);
        return PageResponse.success(dto);
    }

}
