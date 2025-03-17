/*
 * Copyright (c) 2025. Bubble
 */

package com.bubble.pilipili.video.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bubble.pilipili.common.component.EntityConverter;
import com.bubble.pilipili.common.pojo.PageDTO;
import com.bubble.pilipili.common.util.ListUtil;
import com.bubble.pilipili.video.pojo.dto.QueryVideoInfoDTO;
import com.bubble.pilipili.video.pojo.entity.CollectionVideo;
import com.bubble.pilipili.video.pojo.entity.VideoInfo;
import com.bubble.pilipili.video.pojo.req.ChangeCollectionVideoReq;
import com.bubble.pilipili.video.pojo.req.PageQueryCollectionVideoReq;
import com.bubble.pilipili.video.repository.CollectionVideoRepository;
import com.bubble.pilipili.video.repository.VideoInfoRepository;
import com.bubble.pilipili.video.service.CollectionVideoService;
import com.bubble.pilipili.video.service.VideoInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Bubble
 * @date 2025.03.17 17:16
 */
@Service
public class CollectionVideoServiceImpl implements CollectionVideoService {

    @Autowired
    private CollectionVideoRepository collectionVideoRepository;
    @Autowired
    private VideoInfoRepository videoInfoRepository;
    @Autowired
    private VideoInfoServiceImpl videoInfoServiceImpl;

    /**
     * 批量保存收藏夹视频关系
     *
     * @param req
     * @return
     */
    @Override
    public Boolean saveCollectionVideo(ChangeCollectionVideoReq req) {
        return collectionVideoRepository.save(testCollectionVideoList(req.getCollectionVideoList()));
    }

    /**
     * 批量删除收藏夹视频关系
     *
     * @param req
     * @return
     */
    @Override
    public Boolean deleteCollectionVideo(ChangeCollectionVideoReq req) {
        return collectionVideoRepository.delete(testCollectionVideoList(req.getCollectionVideoList()));
    }

    /**
     * 过滤无效数据
     * @param collectionVideoList
     * @return
     */
    private List<CollectionVideo> testCollectionVideoList(List<CollectionVideo> collectionVideoList) {
        return collectionVideoList.stream()
                .filter(cv -> cv.getCollectionId() != null && cv.getVid() != null)
                .collect(Collectors.toList());
    }

    /**
     * 分页查询收藏夹视频
     *
     * @param req
     * @return
     */
    @Override
    public PageDTO<QueryVideoInfoDTO> queryCollectionVideo(PageQueryCollectionVideoReq req) {
        if (req.getCollectionId() == null) {
            return PageDTO.emptyPageDTO();
        }
        Page<CollectionVideo> page =
                collectionVideoRepository.queryByCollectionId(req.getCollectionId(), req.getPageNo(), req.getPageSize());
        List<QueryVideoInfoDTO> dtoList = handleCollectionVideo(page.getRecords());
        return PageDTO.createPageDTO(page, dtoList);
    }

    /**
     * 查询收藏夹视频信息
     * @param collectionVideoList
     * @return
     */
    private List<QueryVideoInfoDTO> handleCollectionVideo(List<CollectionVideo> collectionVideoList) {
        if (ListUtil.isEmpty(collectionVideoList)) {
            return Collections.emptyList();
        }

        List<Integer> vidList = collectionVideoList.stream().map(CollectionVideo::getVid).collect(Collectors.toList());
        List<VideoInfo> videoInfoList = videoInfoRepository.getVideoInfoById(vidList);

        // 查询视频统计数据
        return videoInfoServiceImpl.handleVideoStats(videoInfoList);
    }
}
