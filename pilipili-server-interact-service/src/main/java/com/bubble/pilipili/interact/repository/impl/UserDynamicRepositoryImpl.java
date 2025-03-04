/*
 * Copyright (c) 2025. Bubble
 */

package com.bubble.pilipili.interact.repository.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.bubble.pilipili.common.exception.RepositoryException;
import com.bubble.pilipili.common.util.ListUtil;
import com.bubble.pilipili.interact.mapper.UserDynamicMapper;
import com.bubble.pilipili.interact.pojo.dto.QueryDynamicStatsDTO;
import com.bubble.pilipili.interact.pojo.entity.UserDynamic;
import com.bubble.pilipili.interact.repository.UserDynamicRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

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
    @Transactional
    @Override
    public Boolean saveUserDynamic(UserDynamic userDynamic) {
        Integer did = userDynamic.getDid();
        Integer uid = userDynamic.getUid();
        Integer favor = userDynamic.getFavor();
        Integer repost = userDynamic.getRepost();
        if (did == null || uid == null) {
            return false;
        }
        if (favor == null && repost == null) {
            return false;
        }

        boolean exists = userDynamicMapper.exists(
                new LambdaQueryWrapper<UserDynamic>()
                        .eq(UserDynamic::getDid, did)
                        .eq(UserDynamic::getUid, uid)
        );
        if (exists) {
            // 更新
            LambdaUpdateWrapper<UserDynamic> updateWrapper = new LambdaUpdateWrapper<>();
            updateWrapper.eq(UserDynamic::getDid, did);
            updateWrapper.eq(UserDynamic::getUid, uid);
            if (favor != null) {
                updateWrapper.set(UserDynamic::getFavor, favor);
            }
            if (repost != null) {
                updateWrapper.set(UserDynamic::getRepost, repost);
            }
            int update = userDynamicMapper.update(updateWrapper);
            if (update > 1) {
                throw new RepositoryException("更新数量大于1");
            }
            return update == 1;
        } else {
            // 新增
            try {
                return userDynamicMapper.insert(userDynamic) == 1;
            } catch (DataIntegrityViolationException ex) {
                throw new RepositoryException("新增记录异常");
            }
        }

    }
    /**
     * 查询指定动态的统计数据
     * @param did
     * @return
     */
    @Override
    public QueryDynamicStatsDTO getDynamicStats(Integer did) {
        QueryWrapper<UserDynamic> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("did", "SUM(favor) AS favor", "SUM(repost) AS repost");
        queryWrapper.eq("did", did);

        return doQueryDynamicStats(queryWrapper).get(0);
    }

    /**
     * 批量查询动态统计数据
     * @param didList
     * @return
     */
    @Override
    public List<QueryDynamicStatsDTO> getDynamicStatsBatch(List<Integer> didList) {
        if (ListUtil.isEmpty(didList)) {
            return null;
        }

        QueryWrapper<UserDynamic> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("did", "SUM(favor) AS favor", "SUM(repost) AS repost");
        queryWrapper.in("did", didList);
        queryWrapper.groupBy("did");

        return doQueryDynamicStats(queryWrapper);
    }

    private List<QueryDynamicStatsDTO> doQueryDynamicStats(QueryWrapper<UserDynamic> queryWrapper) {
        return userDynamicMapper.selectList(queryWrapper).stream()
                .map(userDynamic -> {
                    QueryDynamicStatsDTO dto = new QueryDynamicStatsDTO();
                    dto.setDid(userDynamic.getDid());
                    dto.setFavorCount(userDynamic.getFavor());
                    dto.setRepostCount(userDynamic.getRepost());
                    return dto;
                })
                .collect(Collectors.toList());
    }
}
