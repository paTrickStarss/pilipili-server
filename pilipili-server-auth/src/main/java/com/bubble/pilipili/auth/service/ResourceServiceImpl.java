/*
 * Copyright (c) 2024. Bubble
 */

package com.bubble.pilipili.auth.service;

import com.bubble.pilipili.auth.config.ResourceConfigProperties;
import com.bubble.pilipili.auth.entity.ResourceMap;
import com.bubble.pilipili.common.constant.RedisKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.*;

/**
 * @author liweixin@hcrc1.wecom.work
 * @date 2024/10/24
 */
@Service
public class ResourceServiceImpl {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private ResourceConfigProperties resourceConfigProperties;

    @PostConstruct
    public void init() {
        Map<String, List<String>> resourceRolesMap = new TreeMap<>();
        List<ResourceMap> maps = resourceConfigProperties.getPathRoleMap();
        for (ResourceMap map : maps) {
            resourceRolesMap.put(map.getPath(), map.getRole());
        }

        redisTemplate.delete(RedisKey.RESOURCE_ROLES_MAP.name());
        redisTemplate.opsForHash().putAll(RedisKey.RESOURCE_ROLES_MAP.name(), resourceRolesMap);
    }
}
