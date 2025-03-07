/*
 * Copyright (c) 2025. Bubble
 */

package com.bubble.pilipili.video.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bubble.pilipili.common.exception.ServiceOperationException;
import com.bubble.pilipili.common.pojo.PageDTO;
import com.bubble.pilipili.common.util.ListUtil;
import com.bubble.pilipili.video.pojo.converter.VideoInfoConverter;
import com.bubble.pilipili.video.pojo.dto.QueryVideoInfoDTO;
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
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
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
        Page<VideoInfo> videoInfoPage =
                videoInfoRepository.pageQueryVideoInfoByUid(req.getUid(), req.getPageNo(), req.getPageSize());

        List<QueryVideoInfoDTO> dtoList = handleVideoStats(videoInfoPage.getRecords());
        return PageDTO.createPageDTO(
                videoInfoPage.getCurrent(),
                videoInfoPage.getSize(),
                videoInfoPage.getTotal(),
                dtoList
        );
    }

    /**
     * 分页条件查询所有视频
     * @param req
     * @return
     */
    @Override
    public PageDTO<QueryVideoInfoDTO> pageQueryVideoInfo(PageQueryVideoInfoReq req) {
        QueryVideoInfoParam param = VideoInfoConverter.getInstance().copyFieldValue(req, QueryVideoInfoParam.class);
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
    private List<QueryVideoInfoDTO> handleVideoStats(List<VideoInfo> videoInfoList) {
        if (ListUtil.isEmpty(videoInfoList)) {
            return Collections.emptyList();
        }
        List<QueryVideoInfoDTO> dtoList =
                VideoInfoConverter.getInstance().copyFieldValueList(
                        videoInfoList, QueryVideoInfoDTO.class);

        Map<Integer, QueryVideoInfoDTO> dtoMap = dtoList.stream().collect(Collectors.toMap(
                QueryVideoInfoDTO::getVid, Function.identity(), (a, b) -> b
        ));
        // 获取视频统计数据
        List<Integer> vidList = new ArrayList<>(dtoMap.keySet());
        userVideoRepository.getVideoStats(vidList)
                .forEach(stats -> {
                    QueryVideoInfoDTO dto = dtoMap.get(stats.getVid());
                    dto.setFavorCount(stats.getFavorCount());
                    dto.setCoinCount(stats.getCoinCount());
                    dto.setCollectCount(stats.getCollectCount());
                    dto.setRepostCount(stats.getRepostCount());
                    dto.setDewCount(stats.getDewCount());
                });

        return new ArrayList<>(dtoMap.values());
    }
}
