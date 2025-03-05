/*
 * Copyright (c) 2025. Bubble
 */

package com.bubble.pilipili.interact.controller;

import com.bubble.pilipili.common.http.Controller;
import com.bubble.pilipili.common.http.PageQueryResponse;
import com.bubble.pilipili.common.http.SimpleResponse;
import com.bubble.pilipili.common.pojo.PageDTO;
import com.bubble.pilipili.interact.pojo.dto.QueryCommentInfoDTO;
import com.bubble.pilipili.interact.pojo.req.PageQueryCommentInfoReq;
import com.bubble.pilipili.interact.pojo.req.SaveCommentInfoReq;
import com.bubble.pilipili.interact.service.CommentInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

/**
 * @author Bubble
 * @date 2025.03.05 20:30
 */
@RestController
@RequestMapping("/api/interact/comment")
public class CommentController implements Controller {

    @Autowired
    private CommentInfoService commentInfoService;

    /**
     * 新增评论
     * @param req
     * @return
     */
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
    @DeleteMapping("/{cid}")
    public SimpleResponse<String> delete(
            @NotBlank(message = "请传入cid") @PathVariable Integer cid
    ) {
        Boolean b = commentInfoService.deleteCommentInfo(cid);
        return SimpleResponse.result(b);
    }

    /**
     * 查询指定评论信息
     * @param cid
     * @return
     */
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
    @GetMapping("/pageQueryByRela")
    public PageQueryResponse<QueryCommentInfoDTO> pageQueryByRela(
            @Valid @ModelAttribute PageQueryCommentInfoReq req
    ) {
        PageDTO<QueryCommentInfoDTO> dto = commentInfoService.pageQueryCommentInfoByRela(req);
        return PageQueryResponse.success(dto);
    }

    /**
     * 分页查询评论的所有回复
     * @param req
     * @return
     */
    @GetMapping("/pageQueryCommentReply")
    public PageQueryResponse<QueryCommentInfoDTO> pageQueryCommentReply(
            @Valid @ModelAttribute PageQueryCommentInfoReq req
    ) {
        PageDTO<QueryCommentInfoDTO> dto = commentInfoService.pageQueryCommentReply(req);
        return PageQueryResponse.success(dto);
    }

}
