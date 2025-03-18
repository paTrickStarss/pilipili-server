/*
 * Copyright (c) 2025. Bubble
 */

package com.bubble.pilipili.common.util;

import org.aspectj.lang.ProceedingJoinPoint;

import java.util.Map;

/**
 * 通用切面处理行为管理类
 * @author Bubble
 * @date 2025.03.02 20:59
 */
public class AspectHandlerAction {

    /**
     * 控制层日志处理
     * @param joinPoint
     * @param pathMap
     * @return
     * @throws Throwable
     */
    public static Object controllerLoggingAction(
            ProceedingJoinPoint joinPoint,
            Map<String, String> pathMap
    ) throws Throwable {
        Class<?> clz = joinPoint.getTarget().getClass();
        String methodName = joinPoint.getSignature().getName();
        Object[] args = joinPoint.getArgs();

        long l1 = System.currentTimeMillis();
        try {
            Object rt = joinPoint.proceed();
            LogUtil.apiSuccessLog(clz, pathMap.get(getMethodName(clz, methodName)),
                    args, rt, System.currentTimeMillis() - l1);
            return rt;
        } catch (Throwable throwable) {
            LogUtil.apiFailedLog(clz, pathMap.get(getMethodName(clz, methodName)), args, throwable.getMessage(), System.currentTimeMillis() - l1);
//            return ResponseEntity.internalServerError().body(throwable.getMessage());
            throw throwable;
//            return SimpleResponse.error(throwable.getMessage());
        }
    }

    /**
     * 获取限定方法名，例如：Controller.save
     * @param clz
     * @param methodSimpleName
     * @return
     */
    public static String getMethodName(Class<?> clz, String methodSimpleName) {
        return String.join(".", clz.getSimpleName(), methodSimpleName);
    }
}
