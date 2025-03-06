/*
 * Copyright (c) 2025. Bubble
 */

package com.bubble.pilipili.interact.repository.impl;

import com.bubble.pilipili.interact.mapper.UserDynamicMapper;
import com.bubble.pilipili.interact.pojo.dto.QueryDynamicStatsDTO;
import com.bubble.pilipili.interact.pojo.entity.UserDynamic;
import com.bubble.pilipili.interact.repository.UserDynamicRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

/**
 * @author Bubble
 * @date 2025.03.04 14:08
 */
@Component
public class UserDynamicRepositoryImpl implements UserDynamicRepository {

    @Autowired
    private UserDynamicMapper userDynamicMapper;

    /**
     * 保存用户动态互动信息
     * @param userDynamic
     * @return
     */
    @Override
    public Boolean saveUserDynamic(UserDynamic userDynamic) {
        return CommonRepoImpl.save(
                userDynamic,
                UserDynamic::getDid,
                UserDynamic::getUid,
                (updateWrapper, entity) -> {
                    if (entity.getFavor() != null) {
                        updateWrapper.set(UserDynamic::getFavor, entity.getFavor());
                    }
                    if (entity.getRepost() != null) {
                        updateWrapper.set(UserDynamic::getRepost, entity.getRepost());
                    }
                },
                userDynamicMapper
        );
    }
    /**
     * 查询指定动态的统计数据
     * @param did
     * @return
     */
    @Override
    public QueryDynamicStatsDTO getDynamicStats(Integer did) {
        return CommonRepoImpl.getStatsBatch(
                Collections.singletonList(did),
                QueryDynamicStatsDTO.class,
                (entity, dto) -> {
                    dto.setDid(entity.getDid());
                    dto.setFavorCount(entity.getFavor());
                    dto.setRepostCount(entity.getRepost());
                },
                userDynamicMapper,
                "did",
                "favor", "repost"
        ).get(0);
    }

    /**
     * 批量查询动态统计数据
     * @param didList
     * @return
     */
    @Override
    public List<QueryDynamicStatsDTO> getDynamicStats(List<Integer> didList) {
        return CommonRepoImpl.getStatsBatch(
                didList,
                QueryDynamicStatsDTO.class,
                (entity, dto) -> {
                    dto.setDid(entity.getDid());
                    dto.setFavorCount(entity.getFavor());
                    dto.setRepostCount(entity.getRepost());
                },
                userDynamicMapper,
                "did",
                "favor", "repost"
        );
    }
}
