/*
 * Copyright (c) 2024. Bubble
 */

package com.bubble.pilipili.common.component;

import com.bubble.pilipili.common.constant.RedisKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;

/**
 * @author liweixin@hcrc1.wecom.work
 * @date 2024/12/31
 */
@Component
public class SessionManager {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * 检查Token有效性
     * @param username
     * @param jti
     * @return
     */
    public boolean checkToken(String username, String jti) {
//        Object savedJti = redisTemplate.opsForHash().get(RedisKey.USER_TOKEN_MAP.name(), username);
        Object savedJti = redisTemplate.opsForValue().get(getKey(username));
        return savedJti != null && savedJti.equals(jti);
    }

    /**
     * 保存Token
     * @param username
     * @param jti
     */
    public void saveToken(String username, String jti, long expires) {
        redisTemplate.opsForValue().set(getKey(username), jti, Duration.ofSeconds(expires));
//        redisTemplate.opsForHash().put(RedisKey.USER_TOKEN_MAP.name(), username, jti);
    }

    /**
     * 删除Token
     * @param username
     */
    public Boolean removeToken(String username) {
        return redisTemplate.delete(getKey(username));
//        redisTemplate.opsForHash().delete(RedisKey.USER_TOKEN_MAP.name(), username);
    }

    public String getKey(String username) {
        return RedisKey.USER_TOKEN_MAP.getKey() + RedisKey.KEY_DIVIDER.getKey() + username;
    }
}
