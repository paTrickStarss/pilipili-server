/*
 * Copyright (c) 2025. Bubble
 */

package com.bubble.pilipili.video.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bubble.pilipili.common.component.EntityConverter;
import com.bubble.pilipili.common.exception.ServiceOperationException;
import com.bubble.pilipili.common.http.SimpleResponse;
import com.bubble.pilipili.common.pojo.PageDTO;
import com.bubble.pilipili.common.service.InteractStatsAction;
import com.bubble.pilipili.common.util.ListUtil;
import com.bubble.pilipili.common.util.StringUtil;
import com.bubble.pilipili.feign.api.StatsFeignAPI;
import com.bubble.pilipili.feign.api.StatsMQFeignAPI;
import com.bubble.pilipili.feign.pojo.dto.QueryStatsDTO;
import com.bubble.pilipili.feign.pojo.req.SendVideoStatsReq;
import com.bubble.pilipili.feign.pojo.entity.VideoStats;
import com.bubble.pilipili.video.pojo.dto.QueryVideoInfoDTO;
import com.bubble.pilipili.video.pojo.entity.UserVideo;
import com.bubble.pilipili.video.pojo.entity.VideoInfo;
import com.bubble.pilipili.video.pojo.param.QueryVideoInfoParam;
import com.bubble.pilipili.video.pojo.req.CreateVideoInfoReq;
import com.bubble.pilipili.video.pojo.req.PageQueryVideoInfoReq;
import com.bubble.pilipili.video.pojo.req.UpdateVideoInfoReq;
import com.bubble.pilipili.video.repository.UserVideoRepository;
import com.bubble.pilipili.video.repository.VideoInfoRepository;
import com.bubble.pilipili.video.service.VideoInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * 视频信息业务层
 * @author Bubble
 * @date 2025/01/21 15:33
 */
@Slf4j
@Service
public class VideoInfoServiceImpl implements VideoInfoService {

    @Autowired
    private VideoInfoRepository videoInfoRepository;
    @Autowired
    private UserVideoRepository userVideoRepository;

    @Autowired
    private InteractStatsAction interactStatsAction;
    @Autowired
    private EntityConverter entityConverter;

    @Autowired
    private StatsMQFeignAPI statsMQFeignAPI;
    @Autowired
    private StatsFeignAPI statsFeignAPI;

    /**
     * 新增视频信息
     * @param req
     * @return
     */
    @Transactional
    @Override
    public Boolean saveVideoInfo(CreateVideoInfoReq req) {
        VideoInfo videoInfo = entityConverter.copyFieldValue(req, VideoInfo.class);
        return videoInfoRepository.saveVideoInfo(videoInfo);
    }

    /**
     * 更新视频信息
     * @param req
     * @return
     */
    @Transactional
    @Override
    public Boolean updateVideoInfo(UpdateVideoInfoReq req) {
        VideoInfo videoInfo = entityConverter.copyFieldValue(req, VideoInfo.class);
        if (videoInfo.getVid() == null) {
            throw ServiceOperationException.emptyField("vid");
//            return false;
        }

        // MybatisPlus中updateById方法是增量字段更新，不是全字段更新，不需要手动获取原数据再填入更新字段（不需要上面的做法）
        return videoInfoRepository.updateVideoInfo(videoInfo);
    }

    /**
     * 点赞视频
     *
     * @param vid
     * @param uid
     * @return
     */
    @Transactional(isolation = Isolation.READ_COMMITTED)
    @Override
    public Boolean favorVideoInfo(Integer vid, Integer uid) {
        Boolean b = updateVideoInteract(
                vid, uid,
                uv -> uv.setFavor(1),
                stats -> stats.setFavorCount(1L)
        );
//        revokeDewVideoInfo(vid, uid);
        return b;
    }

    /**
     * 取消点赞视频
     *
     * @param vid
     * @param uid
     * @return
     */
    @Transactional
    @Override
    public Boolean revokeFavorVideoInfo(Integer vid, Integer uid) {
        return updateVideoInteract(
                vid, uid,
                uv -> uv.setFavor(0),
                stats -> stats.setFavorCount(-1L)
        );
    }

    /**
     * 投币视频
     *
     * @param vid
     * @param uid
     * @return
     */
    @Transactional
    @Override
    public Boolean coinVideoInfo(Integer vid, Integer uid) {
        return updateVideoInteract(
                vid, uid,
                uv -> uv.setCoin(1),
                stats -> stats.setCoinCount(1L)
        );
    }

    /**
     * 收藏视频
     *
     * @param vid
     * @param uid
     * @return
     */
    @Transactional
    @Override
    public Boolean collectVideoInfo(Integer vid, Integer uid) {
        return updateVideoInteract(
                vid, uid,
                uv -> uv.setCollect(1),
                stats -> stats.setCollectCount(1L)
        );
    }

    /**
     * 取消收藏
     *
     * @param vid
     * @param uid
     * @return
     */
    @Transactional
    @Override
    public Boolean revokeCollectVideoInfo(Integer vid, Integer uid) {
        return updateVideoInteract(
                vid, uid,
                uv -> uv.setCollect(0),
                stats -> stats.setCollectCount(-1L)
        );
    }

    /**
     * 转发视频
     *
     * @param vid
     * @param uid
     * @return
     */
    @Transactional
    @Override
    public Boolean repostVideoInfo(Integer vid, Integer uid) {
        return updateVideoInteract(
                vid, uid,
                uv -> uv.setRepost(1),
                stats -> stats.setRepostCount(1L)
        );
    }

    /**
     * 取消转发视频
     *
     * @param vid
     * @param uid
     * @return
     */
    @Transactional
    @Override
    public Boolean revokeRepostVideoInfo(Integer vid, Integer uid) {
        return updateVideoInteract(
                vid, uid,
                uv -> uv.setRepost(0),
                stats -> stats.setRepostCount(-1L)
        );
    }

    /**
     * 点踩视频
     *
     * @param vid
     * @param uid
     * @return
     */
    @Transactional
    @Override
    public Boolean dewVideoInfo(Integer vid, Integer uid) {
        Boolean b = updateVideoInteract(
                vid, uid,
                uv -> uv.setDew(1),
                stats -> stats.setDewCount(1L)
        );
//        revokeFavorVideoInfo(vid, uid);
        return b;
    }

    /**
     * 取消点踩视频
     *
     * @param vid
     * @param uid
     * @return
     */
    @Transactional
    @Override
    public Boolean revokeDewVideoInfo(Integer vid, Integer uid) {
        return updateVideoInteract(
                vid, uid,
                uv -> uv.setDew(0),
                stats -> stats.setDewCount(-1L)
        );
    }

    /**
     * 删除视频信息
     * @param vid
     * @return
     */
    @Transactional
    @Override
    public Boolean deleteVideoInfo(Integer vid) {
        return videoInfoRepository.deleteVideoInfo(vid);
    }

    /**
     * @param vid
     * @return
     */
    @Override
    public QueryVideoInfoDTO getVideoInfoById(Integer vid) {
        VideoInfo videoInfo = videoInfoRepository.getVideoInfoById(vid);
        List<QueryVideoInfoDTO> dtoList = handleVideoStats(Collections.singletonList(videoInfo));
        return ListUtil.isEmpty(dtoList) ? null : dtoList.get(0);
    }

    /**
     * 分页查询用户视频
     * @param req
     * @return
     */
    @Override
    public PageDTO<QueryVideoInfoDTO> pageQueryVideoInfoByUid(PageQueryVideoInfoReq req) {

        // 查询结果按投稿时间降序排序
        Page<VideoInfo> videoInfoPage =
                videoInfoRepository.pageQueryVideoInfoByUid(
                        req.getUid(), req.getPageNo(), req.getPageSize(),
                        true, false, Collections.singletonList(VideoInfo::getUploadTime)
                );

        List<QueryVideoInfoDTO> dtoList = handleVideoStats(videoInfoPage.getRecords());
        return PageDTO.createPageDTO(
                videoInfoPage.getCurrent(),
                videoInfoPage.getSize(),
                videoInfoPage.getTotal(),
                dtoList
        );
    }

    /**
     * @param req
     * @return
     */
    @Override
    public PageDTO<QueryVideoInfoDTO> pageQueryPassedVideoInfoByUid(PageQueryVideoInfoReq req) {

        // 查询结果按投稿时间降序排序
        Page<VideoInfo> videoInfoPage =
                videoInfoRepository.pageQueryPassedVideoInfoByUid(
                        req.getUid(), req.getPageNo(), req.getPageSize(),
                        true, false, Collections.singletonList(VideoInfo::getUploadTime)
                );

        List<QueryVideoInfoDTO> dtoList = handleVideoStats(videoInfoPage.getRecords());
        return PageDTO.createPageDTO(
                videoInfoPage.getCurrent(),
                videoInfoPage.getSize(),
                videoInfoPage.getTotal(),
                dtoList
        );
    }

    /**
     * 分页条件查询所有已上架视频
     * @param req
     * @return
     */
    @Override
    public PageDTO<QueryVideoInfoDTO> pageQueryVideoInfo(PageQueryVideoInfoReq req) {
        QueryVideoInfoParam param = entityConverter.copyFieldValue(req, QueryVideoInfoParam.class);
        Page<VideoInfo> videoInfoPage =
                videoInfoRepository.pageQueryVideoInfo(param, req.getPageNo(), req.getPageSize());

        List<QueryVideoInfoDTO> dtoList = handleVideoStats(videoInfoPage.getRecords());
        return PageDTO.createPageDTO(
                videoInfoPage.getCurrent(),
                videoInfoPage.getSize(),
                videoInfoPage.getTotal(),
                dtoList
        );
    }

    /**
     * 获取视频统计数据并返回DTO
     * @param videoInfoList
     * @return
     */
    List<QueryVideoInfoDTO> handleVideoStats(List<VideoInfo> videoInfoList) {
        if (ListUtil.isEmpty(videoInfoList)) {
            return Collections.emptyList();
        }
        List<QueryVideoInfoDTO> dtoList =
                entityConverter.copyFieldValueList(
                        videoInfoList, QueryVideoInfoDTO.class);

        // 获取视频统计数据
        List<Integer> vidList = dtoList.stream().map(QueryVideoInfoDTO::getVid).collect(Collectors.toList());
        // ok: feignAPI获取视频统计数据
        SimpleResponse<QueryStatsDTO<VideoStats>> response = statsFeignAPI.getVideoStats(vidList);
        Map<Integer, VideoStats> statsMap = response.getData().getStatsMap();
        dtoList.forEach(dto -> {
            VideoStats stats = statsMap.get(dto.getVid());
            if (stats != null) {
                dto.setViewCount(stats.getViewCount());
                dto.setDanmakuCount(stats.getDanmakuCount());
                dto.setFavorCount(stats.getFavorCount());
                dto.setCoinCount(stats.getCoinCount());
                dto.setCollectCount(stats.getCollectCount());
                dto.setRepostCount(stats.getRepostCount());
                dto.setDewCount(stats.getDewCount());
            }

            // tag转数组
            dto.setTagList(getTagList(dto.getTag()));
        });

        return dtoList;
    }

    /**
     * 更新视频互动关系数据
     * @param vid
     * @param uid
     * @param interactConsumer
     * @param statsConsumer
     * @return
     */
    private Boolean updateVideoInteract(
            Integer vid, Integer uid,
            Consumer<UserVideo> interactConsumer,
            Consumer<VideoStats> statsConsumer
    ) {
        try {
            Boolean b = interactStatsAction.updateInteract(
                    UserVideo.class,
                    vid, uid,
                    UserVideo::setVid,
                    userVideoRepository,
                    interactConsumer
            );
            if (b) {
                // 推送统计数据更新消息
                VideoStats stats = new VideoStats();
                statsConsumer.accept(stats);
                SendVideoStatsReq req =
                        entityConverter.copyFieldValue(stats, SendVideoStatsReq.class);
                req.setVid(vid);
                statsMQFeignAPI.sendVideoStats(req);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            log.warn(e.getMessage());
            throw new ServiceOperationException("更新视频互动关系数据异常");
        }
    }

    private List<String> getTagList(String tag) {
        if (StringUtil.isEmpty(tag)) {
            return Collections.emptyList();
        }
        return Arrays
                .stream(tag.split(","))
                .map(String::trim)
                .collect(Collectors.toList());
    }
}
