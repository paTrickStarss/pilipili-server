/*
 * Copyright (c) 2025. Bubble
 */

package com.bubble.pilipili.video.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bubble.pilipili.common.exception.ServiceOperationException;
import com.bubble.pilipili.common.pojo.PageDTO;
import com.bubble.pilipili.video.pojo.converter.VideoInfoConverter;
import com.bubble.pilipili.video.pojo.dto.QueryVideoInfoDTO;
import com.bubble.pilipili.video.pojo.entity.VideoInfo;
import com.bubble.pilipili.video.pojo.param.QueryVideoInfoParam;
import com.bubble.pilipili.video.pojo.req.CreateVideoInfoReq;
import com.bubble.pilipili.video.pojo.req.PageQueryVideoInfoReq;
import com.bubble.pilipili.video.pojo.req.UpdateVideoInfoReq;
import com.bubble.pilipili.video.repository.VideoInfoRepository;
import com.bubble.pilipili.video.service.VideoInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

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

    /**
     * 新增视频信息
     * @param req
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public Boolean saveVideoInfo(CreateVideoInfoReq req) {
        VideoInfo videoInfo = VideoInfoConverter.getInstance().copyFieldValue(req, VideoInfo.class);
        return videoInfoRepository.saveVideoInfo(videoInfo);
    }

    /**
     * 更新视频信息
     * @param req
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public Boolean updateVideoInfo(UpdateVideoInfoReq req) {
        VideoInfo videoInfo = VideoInfoConverter.getInstance().copyFieldValue(req, VideoInfo.class);
        if (videoInfo.getVid() == null) {
            throw ServiceOperationException.emptyField("vid");
//            return false;
        }

//        VideoInfo originRow = videoInfoRepository.getVideoInfoById(videoInfo.getVid());
//        if (originRow == null) {
//            return false;
//        }
//        VideoInfo updateVideoInfo = VideoInfoConverter.getInstance().copyUpdateFieldValue(originRow, videoInfo);
//        return videoInfoRepository.updateVideoInfo(updateVideoInfo);

        // MybatisPlus中updateById方法是增量字段更新，不是全字段更新，不需要手动获取原数据再填入更新字段（不需要上面的做法）
        return videoInfoRepository.updateVideoInfo(videoInfo);
    }

    /**
     * 删除视频信息
     * @param vid
     * @return
     */
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
        return VideoInfoConverter.getInstance().copyFieldValue(videoInfo, QueryVideoInfoDTO.class);
    }

    /**
     * 分页查询用户视频
     * @param req
     * @return
     */
    @Override
    public PageDTO<QueryVideoInfoDTO> pageQueryVideoInfoByUid(PageQueryVideoInfoReq req) {
        Page<VideoInfo> videoInfoPage = videoInfoRepository.pageQueryVideoInfoByUid(req.getUid(), req.getPageNo(), req.getPageSize());
        return wrapperPageDTO(videoInfoPage);
    }

    /**
     * 分页条件查询所有视频
     * @param req
     * @return
     */
    @Override
    public PageDTO<QueryVideoInfoDTO> pageQueryVideoInfo(PageQueryVideoInfoReq req) {
        QueryVideoInfoParam param = VideoInfoConverter.getInstance().copyFieldValue(req, QueryVideoInfoParam.class);
        Page<VideoInfo> videoInfoPage = videoInfoRepository.pageQueryVideoInfo(param, req.getPageNo(), req.getPageSize());
        return wrapperPageDTO(videoInfoPage);
    }

    private PageDTO<QueryVideoInfoDTO> wrapperPageDTO(Page<VideoInfo> videoInfoPage) {
        List<QueryVideoInfoDTO> dtoList = new ArrayList<>();
        videoInfoPage.getRecords().forEach(videoInfo -> {
            dtoList.add(VideoInfoConverter.getInstance().copyFieldValue(videoInfo, QueryVideoInfoDTO.class));
        });
        return PageDTO.createPageDTO(videoInfoPage.getCurrent(), videoInfoPage.getSize(), videoInfoPage.getTotal(), dtoList);
    }
}
