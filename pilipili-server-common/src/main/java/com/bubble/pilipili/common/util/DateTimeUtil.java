/*
 * Copyright (c) 2025. Bubble
 */

package com.bubble.pilipili.common.util;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * @author Bubble
 * @date 2025/01/24 16:05
 */
public class DateTimeUtil {

    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static String now() {
        return LocalDateTime.now().format(formatter);
    }

    public static String timeFormat(Date date) {
        return sdf.format(date);
    }
    public static String timeFormat(LocalDateTime localDateTime) {
        return localDateTime.format(formatter);
    }
}
