/*
 * Copyright (c) 2025. Bubble
 */

package com.bubble.pilipili.video.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bubble.pilipili.common.component.EntityConverter;
import com.bubble.pilipili.common.pojo.PageDTO;
import com.bubble.pilipili.common.pojo.converter.BaseConverter;
import com.bubble.pilipili.common.util.ListUtil;
import com.bubble.pilipili.video.pojo.dto.QueryCollectionInfoDTO;
import com.bubble.pilipili.video.pojo.entity.CollectionInfo;
import com.bubble.pilipili.video.pojo.req.PageQueryCollectionInfoReq;
import com.bubble.pilipili.video.pojo.req.SaveCollectionInfoReq;
import com.bubble.pilipili.video.pojo.req.UpdateCollectionInfoReq;
import com.bubble.pilipili.video.repository.CollectionInfoRepository;
import com.bubble.pilipili.video.repository.CollectionVideoRepository;
import com.bubble.pilipili.video.service.CollectionInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Bubble
 * @date 2025.03.17 16:29
 */
@Service
public class CollectionInfoServiceImpl implements CollectionInfoService {

    @Autowired
    private CollectionInfoRepository collectionInfoRepository;

    @Autowired
    private CollectionVideoRepository collectionVideoRepository;
    
    @Autowired
    private EntityConverter entityConverter;

    /**
     * 保存收藏夹信息
     *
     * @param req
     * @return
     */
    @Transactional
    @Override
    public Boolean saveCollectionInfo(SaveCollectionInfoReq req) {
        CollectionInfo collectionInfo = entityConverter.copyFieldValue(req, CollectionInfo.class);
        return collectionInfoRepository.save(collectionInfo);
    }

    /**
     * 更新收藏夹信息
     *
     * @param req
     * @return
     */
    @Transactional
    @Override
    public Boolean updateCollectionInfo(UpdateCollectionInfoReq req) {
        CollectionInfo collectionInfo = entityConverter.copyFieldValue(req, CollectionInfo.class);
        return collectionInfoRepository.update(collectionInfo);
    }

    /**
     * 删除收藏夹
     *
     * @param collectionId
     * @return
     */
    @Override
    public Boolean deleteCollectionInfo(Integer collectionId) {
        // 删除收藏夹包含的视频关系
        collectionVideoRepository.deleteByCollectionId(collectionId);
        return collectionInfoRepository.delete(collectionId);
    }

    /**
     * 查询指定收藏夹信息
     *
     * @param collectionId
     * @return
     */
    @Override
    public QueryCollectionInfoDTO getCollectionInfo(Integer collectionId) {
        CollectionInfo entity = collectionInfoRepository.query(collectionId);
        return handleCollectionInfo(Collections.singletonList(entity)).get(0);
    }

    /**
     * 分页查询用户收藏夹信息
     *
     * @param req
     * @return
     */
    @Override
    public PageDTO<QueryCollectionInfoDTO> queryCollectionInfoByUid(PageQueryCollectionInfoReq req) {
        if (req.getUid() == null) {
            return PageDTO.emptyPageDTO();
        }
        Page<CollectionInfo> page =
                collectionInfoRepository.queryByUid(req.getUid(), req.getPageNo(), req.getPageSize());

        List<QueryCollectionInfoDTO> dtoList = handleCollectionInfo(page.getRecords());

        return PageDTO.createPageDTO(page, dtoList);
    }

    /**
     * 查询收藏夹视频数量统计数据
     * @param collectionInfoList
     * @return
     */
    private List<QueryCollectionInfoDTO> handleCollectionInfo(List<CollectionInfo> collectionInfoList) {
        if (ListUtil.isEmpty(collectionInfoList)) {
            return Collections.emptyList();
        }

        List<Integer> idList =
                collectionInfoList.stream().map(CollectionInfo::getCollectionId).collect(Collectors.toList());
        Map<Long, Long> countMap = collectionVideoRepository.countByCollectionId(idList);
        List<QueryCollectionInfoDTO> dtoList =
                entityConverter.copyFieldValueList(collectionInfoList, QueryCollectionInfoDTO.class);

        dtoList.forEach(dto -> {
            Long count = countMap.get(new Long(dto.getCollectionId()));
            if (count != null) {
                dto.setVideoCount(count);
            }
        });

        return dtoList;
    }
}
