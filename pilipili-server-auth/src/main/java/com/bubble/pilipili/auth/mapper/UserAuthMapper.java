/*
 * Copyright (c) 2024-2025. Bubble
 */

package com.bubble.pilipili.auth.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bubble.pilipili.auth.entity.UserAuth;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author liweixin@hcrc1.wecom.work
 * @date 2024/10/24
 */
@Mapper
public interface UserAuthMapper extends BaseMapper<UserAuth> {
}
