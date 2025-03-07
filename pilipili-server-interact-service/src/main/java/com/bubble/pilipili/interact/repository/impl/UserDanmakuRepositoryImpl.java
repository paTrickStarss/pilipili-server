/*
 * Copyright (c) 2025. Bubble
 */

package com.bubble.pilipili.interact.repository.impl;

import com.bubble.pilipili.common.util.CommonRepoImpl;
import com.bubble.pilipili.interact.mapper.UserDanmakuMapper;
import com.bubble.pilipili.interact.pojo.dto.QueryDanmakuStatsDTO;
import com.bubble.pilipili.interact.pojo.entity.UserDanmaku;
import com.bubble.pilipili.interact.repository.UserDanmakuRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author Bubble
 * @date 2025.03.06 14:39
 */
@Component
public class UserDanmakuRepositoryImpl implements UserDanmakuRepository {

    @Autowired
    private UserDanmakuMapper userDanmakuMapper;

    /**
     * @param userDanmaku
     * @return
     */
    @Override
    public Boolean saveUserDanmaku(UserDanmaku userDanmaku) {
        return CommonRepoImpl.save(
                userDanmaku,
                UserDanmaku::getDanmakuId,
                UserDanmaku::getUid,
                (updateWrapper, entity) -> {
                    if (entity.getFavor() != null) {
                        updateWrapper.set(UserDanmaku::getFavor, entity.getFavor());
                    }
                    if (entity.getDew() != null) {
                        updateWrapper.set(UserDanmaku::getDew, entity.getDew());
                    }
                },
                userDanmakuMapper
        );
    }

    /**
     * @param danmakuIdList
     * @return
     */
    @Override
    public List<QueryDanmakuStatsDTO> getDanmakuStats(List<Integer> danmakuIdList) {
        return CommonRepoImpl.getStatsBatch(
                danmakuIdList,
                QueryDanmakuStatsDTO.class,
                (userDanmaku, dto) -> {
                    dto.setDanmakuId(userDanmaku.getDanmakuId());
                    dto.setFavorCount(userDanmaku.getFavor());
                    dto.setDewCount(userDanmaku.getDew());
                },
                userDanmakuMapper,
                "danmaku_id",
                "favor", "dew"
        );
    }
}
