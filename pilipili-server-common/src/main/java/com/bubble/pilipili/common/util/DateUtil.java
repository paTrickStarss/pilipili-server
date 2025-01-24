/*
 * Copyright (c) 2025. Bubble
 */

package com.bubble.pilipili.common.util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author Bubble
 * @date 2025/01/24 16:05
 */
public class DateUtil {

    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static String getCurrentTime() {
        return sdf.format(new Date());
    }

    public static String formatDate(Date date) {
        return sdf.format(date);
    }
}
