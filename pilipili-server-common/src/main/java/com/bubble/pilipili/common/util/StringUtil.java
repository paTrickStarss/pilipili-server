/*
 * Copyright (c) 2024-2025. Bubble
 */

package com.bubble.pilipili.common.util;

/**
 * 字符串工具类
 * @author liweixin@hcrc1.wecom.work
 * @date 2024/12/25
 */
public class StringUtil {

    /**
     * 字符串是否为null或空串
     * @param str
     * @return
     */
    public static boolean isEmpty(String str) {
        return str == null || str.isEmpty();
    }

    /**
     * 字符串是否不为空
     * @param str
     * @return
     */
    public static boolean isNotEmpty(String str) {
        return !isEmpty(str);
    }
}
