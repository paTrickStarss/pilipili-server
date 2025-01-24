/*
 * Copyright (c) 2025. Bubble
 */

package com.bubble.pilipili.common.util;

import lombok.extern.slf4j.Slf4j;

/**
 * @author Bubble
 * @date 2025/01/24 15:46
 */
@Slf4j
public class LogUtil {

    private static final String successLogTemplate =
            "\nClass: {}" +
            "\nPath: {}" +
            "\nRequest Param/Body: {}" +
            "\nResponse Body: {}" +
            "\nElapsed Time: {} ms";
    private static final String failedLogTemplate =
            "\nClass: {}" +
            "\nPath: {}" +
            "\nRequest Param/Body: {}" +
            "\nError message: {}" +
            "\nElapsed Time: {} ms";

    public static void apiSuccessLog(Class<?> clz, String apiPath, Object req, Object resp, long elapsedTime) {
        log.info(successLogTemplate, clz.getCanonicalName(), apiPath, req, resp, elapsedTime);
    }
    public static void apiFailedLog(Class<?> clz, String apiPath, Object req, Object msg, long elapsedTime) {
        log.warn(failedLogTemplate, clz.getCanonicalName(), apiPath, req, msg, elapsedTime);
    }

}
