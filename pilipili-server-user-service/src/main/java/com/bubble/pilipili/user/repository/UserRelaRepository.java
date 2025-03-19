/*
 * Copyright (c) 2025. Bubble
 */

package com.bubble.pilipili.user.repository;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bubble.pilipili.user.pojo.entity.UserRela;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @author Bubble
 * @date 2025.03.07 14:37
 */
@Repository
public interface UserRelaRepository {

    /**
     * 保存用户关系
     * @param userRela
     * @return
     */
    Boolean saveUserRela(UserRela userRela);

    /**
     * 删除用户关系
     * @param fromUid
     * @param toUid
     * @return
     */
    Boolean deleteUserRela(Integer fromUid, Integer toUid);

    /**
     * 查询是否存在关系
     * @param fromUid
     * @param toUid
     * @return
     */
    Boolean existUserRela(Integer fromUid, Integer toUid);

    /**
     * 批量查询关注数或粉丝数
     * @param uidList
     * @param isToUid false-查询关注数，true-查询粉丝数
     * @return
     */
    Map<Long, Long> countUserRela(List<Integer> uidList, boolean isToUid);

    /**
     * 分页查询关注用户
     * @param fromUid
     * @return
     */
    Page<UserRela> pageQueryUserRelaByFromUid(Integer fromUid, boolean special, Long pageNo, Long pageSize);

    /**
     * 分页查询粉丝用户
     * @param toUid
     * @return
     */
    Page<UserRela> pageQueryUserRelaByToUid(Integer toUid, Long pageNo, Long pageSize);
}
