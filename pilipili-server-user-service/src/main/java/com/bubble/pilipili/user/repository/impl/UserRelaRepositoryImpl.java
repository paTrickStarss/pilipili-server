/*
 * Copyright (c) 2025. Bubble
 */

package com.bubble.pilipili.user.repository.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bubble.pilipili.common.util.CommonRepoImpl;
import com.bubble.pilipili.common.util.ListUtil;
import com.bubble.pilipili.user.mapper.UserRelaMapper;
import com.bubble.pilipili.user.pojo.entity.UserRela;
import com.bubble.pilipili.user.repository.UserRelaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Bubble
 * @date 2025.03.07 14:44
 */
@Component
public class UserRelaRepositoryImpl implements UserRelaRepository {

    @Autowired
    private UserRelaMapper userRelaMapper;

    /**
     * 保存用户关系
     * @param userRela
     * @return
     */
    @Override
    public Boolean saveUserRela(UserRela userRela) {
        return CommonRepoImpl.save(
                userRela,
                UserRela::getFromUid,
                UserRela::getToUid,
                (updateWrapper, entity) -> {
                    if (entity.getSpecial() != null) {
                        updateWrapper.set(UserRela::getSpecial, entity.getSpecial());
                    }
                },
                userRelaMapper
        );
    }

    /**
     * 删除用户关系
     * @param fromUid
     * @param toUid
     * @return
     */
    @Override
    public Boolean deleteUserRela(Integer fromUid, Integer toUid) {
        return userRelaMapper.delete(
                new LambdaQueryWrapper<UserRela>()
                        .eq(UserRela::getFromUid, fromUid)
                        .eq(UserRela::getToUid, toUid)
        ) == 1;
    }

    /**
     * 批量查询关注数或粉丝数
     * @param uidList
     * @param isToUid false-查询关注数，true-查询粉丝数
     * @return
     */
    @Override
    public Map<Long, Long> countUserRela(List<Integer> uidList, boolean isToUid) {
        if (ListUtil.isEmpty(uidList)) {
            return Collections.emptyMap();
        }

        String idFieldName = isToUid ? "to_uid" : "from_uid";
        QueryWrapper<UserRela> qw = new QueryWrapper<>();
        qw.select(idFieldName, "count(*) AS count");
        qw.in(idFieldName, uidList);
        qw.groupBy(idFieldName);

        Map<Long, Long> countMap = new HashMap<>((int) (uidList.size()/0.75f));
        userRelaMapper.selectMaps(qw)
                .forEach(row -> countMap.put((Long) row.get(idFieldName), (Long) row.get("count")));

        return countMap;
    }

    /**
     * 分页查询关注用户
     * @param fromUid
     * @param pageNo
     * @param pageSize
     * @return
     */
    @Override
    public Page<UserRela> pageQueryUserRelaByFromUid(Integer fromUid, boolean special, Long pageNo, Long pageSize) {
        Page<UserRela> page = new Page<>(pageNo, pageSize);
        LambdaQueryWrapper<UserRela> lqw = new LambdaQueryWrapper<UserRela>()
                .eq(UserRela::getFromUid, fromUid);
        if (special) {
            lqw.eq(UserRela::getSpecial, 1);
        }

        return userRelaMapper.selectPage(page, lqw);
    }

    /**
     * 分页查询粉丝用户
     * @param toUid
     * @param pageNo
     * @param pageSize
     * @return
     */
    @Override
    public Page<UserRela> pageQueryUserRelaByToUid(Integer toUid, Long pageNo, Long pageSize) {
        Page<UserRela> page = new Page<>(pageNo, pageSize);
        LambdaQueryWrapper<UserRela> lqw = new LambdaQueryWrapper<UserRela>()
                .eq(UserRela::getToUid, toUid);

        return userRelaMapper.selectPage(page, lqw);
    }
}
