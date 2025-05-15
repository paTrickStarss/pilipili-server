/*
 * Copyright (c) 2025. Bubble
 */

package com.bubble.pilipili.common.util;

import com.alibaba.fastjson2.JSON;
import com.bubble.pilipili.common.constant.AuthConstant;
import com.bubble.pilipili.common.constant.UserRole;
import com.bubble.pilipili.common.exception.ForbiddenException;
import com.bubble.pilipili.common.pojo.JwtPayload;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 请求处理工具类
 * @author Bubble
 * @date 2025.05.15 17:05
 */
@Slf4j
public class RequestUtil {

    private static final Map<String, JwtPayload> jwtPayloadCache = new ConcurrentHashMap<>();

    /**
     * 从请求头获取JWT内容
     * @param request
     * @return
     */
    public static JwtPayload getJwtPayload(HttpServletRequest request) {
        String rawPayload = request.getHeader(AuthConstant.JWT_PAYLOAD_HEADER);
        if (StringUtil.isEmpty(rawPayload)) {
            return new JwtPayload();
        }

        JwtPayload payloadCache = jwtPayloadCache.get(rawPayload);
        if (payloadCache == null) {
            try {
                // rawPayload不为空理论上这里生成的payload也不为空
                JwtPayload payload = JSON.parseObject(rawPayload, JwtPayload.class);
                jwtPayloadCache.put(rawPayload, payload);
                return payload;
            } catch (Exception e) {
                log.error("JwtPayload转化异常：{}", e.getMessage(), e);
                return new JwtPayload();
            }
        }
        return payloadCache;
    }

    /**
     * 从请求头JWT载荷获取用户名
     * @param request
     * @return
     */
    public static String getUsername(HttpServletRequest request) {
        return getJwtPayload(request).getUsername();
    }

    /**
     * 用户权限校验
     * @param request
     * @param paramUserName
     */
    public static void userIdentify(HttpServletRequest request, String paramUserName) {
        if (StringUtil.isEmpty(paramUserName)) {
            throw new ForbiddenException("请求用户名参数异常");
        }
        JwtPayload jwtPayload = getJwtPayload(request);
        String username = jwtPayload.getUsername();
        List<String> authorities = jwtPayload.getAuthorities();
        boolean isAdmin = authorities.contains(UserRole.ROLE_ADMIN.getValue());
        if (StringUtil.isEmpty(username)) {
            throw new ForbiddenException("请求令牌用户名不存在");
        }
        if (!isAdmin && !username.equals(paramUserName)) {
            throw new ForbiddenException("请求用户名不匹配");
        }
    }

    /**
     * 管理员权限校验
     * @param request
     */
    public static void adminIdentify(HttpServletRequest request) {
        List<String> authorities = getJwtPayload(request).getAuthorities();
        boolean isAdmin = authorities.contains(UserRole.ROLE_ADMIN.getValue());
        if (!isAdmin) {
            throw new ForbiddenException("请求权限不足");
        }
    }
}
