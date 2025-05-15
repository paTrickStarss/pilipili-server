/*
 * Copyright (c) 2025. Bubble
 */

package com.bubble.pilipili.video.service;

import com.bubble.pilipili.common.pojo.PageDTO;
import com.bubble.pilipili.video.pojo.dto.QueryCategoryDTO;
import com.bubble.pilipili.video.pojo.dto.QueryUserVideoDTO;
import com.bubble.pilipili.video.pojo.dto.QueryVideoInfoDTO;
import com.bubble.pilipili.video.pojo.req.CreateVideoInfoReq;
import com.bubble.pilipili.video.pojo.req.PageQueryVideoInfoReq;
import com.bubble.pilipili.feign.pojo.req.UpdateVideoInfoReq;

import java.util.List;

/**
 * @author Bubble
 * @date 2025/01/21 15:28
 */
public interface VideoInfoService {

    /**
     * 保存视频信息
     * @param req
     * @return
     */
    Boolean saveVideoInfo(CreateVideoInfoReq req);

    /**
     * 更新视频信息
     * @param req
     * @return
     */
    Boolean updateVideoInfo(UpdateVideoInfoReq req);

    /**
     * 点赞视频
     * @param vid
     * @param uid
     * @return
     */
    Boolean favorVideoInfo(Integer vid, Integer uid);

    /**
     * 取消点赞视频
     * @param vid
     * @param uid
     * @return
     */
    Boolean revokeFavorVideoInfo(Integer vid, Integer uid);

    /**
     * 投币视频
     * @param vid
     * @param uid
     * @return
     */
    Boolean coinVideoInfo(Integer vid, Integer uid);

    /**
     * 收藏视频
     * @param vid
     * @param uid
     * @return
     */
    Boolean collectVideoInfo(Integer vid, Integer uid);

    /**
     * 取消收藏
     * @param vid
     * @param uid
     * @return
     */
    Boolean revokeCollectVideoInfo(Integer vid, Integer uid);

    /**
     * 一键三连
     * @param vid
     * @param uid
     * @return
     */
    Boolean tripleInteractVideoInfo(Integer vid, Integer uid);

    /**
     * 转发视频
     * @param vid
     * @param uid
     * @return
     */
    Boolean repostVideoInfo(Integer vid, Integer uid);

    /**
     * 取消转发视频
     * @param vid
     * @param uid
     * @return
     */
    Boolean revokeRepostVideoInfo(Integer vid, Integer uid);

    /**
     * 点踩视频
     * @param vid
     * @param uid
     * @return
     */
    Boolean dewVideoInfo(Integer vid, Integer uid);

    /**
     * 取消点踩视频
     * @param vid
     * @param uid
     * @return
     */
    Boolean revokeDewVideoInfo(Integer vid, Integer uid);

    /**
     * 删除视频信息
     * @param vid
     * @return
     */
    Boolean deleteVideoInfo(Integer vid);

    /**
     * 查询指定视频信息
     * @param vid
     * @return
     */
    QueryVideoInfoDTO getVideoInfoById(Integer vid);

    /**
     * 分页查询用户所有视频（用户个人空间用，上传中、审核中、审核通过状态的视频）
     * @param req
     * @return
     */
    PageDTO<QueryVideoInfoDTO> pageQueryVideoInfoByUid(PageQueryVideoInfoReq req);

    /**
     * 分页查询用户所有视频（管理员用，所有状态的视频）
     * @param req
     * @return
     */
    PageDTO<QueryVideoInfoDTO> pageQueryAllVideoInfoByUid(PageQueryVideoInfoReq req);

    /**
     * 分页查询用户已上架视频（对外展示用）
     * @param req
     * @return
     */
    PageDTO<QueryVideoInfoDTO> pageQueryPassedVideoInfoByUid(PageQueryVideoInfoReq req);

    /**
     * 分页条件查询所有已上架视频
     * @param req
     * @return
     */
    PageDTO<QueryVideoInfoDTO> pageQueryVideoInfo(PageQueryVideoInfoReq req);

    /**
     * 查询用户视频互动状态
     * @param vid
     * @param uid
     * @return
     */
    QueryUserVideoDTO getUserVideo(Integer vid, Integer uid);

    /**
     * 查询分区列表
     * @return
     */
    List<QueryCategoryDTO> queryCategoryList();
}
