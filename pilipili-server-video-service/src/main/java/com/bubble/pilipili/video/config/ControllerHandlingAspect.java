/*
 * Copyright (c) 2025. Bubble
 */

package com.bubble.pilipili.video.config;

import com.bubble.pilipili.common.http.Response;
import com.bubble.pilipili.common.util.AnnotationUtil;
import com.bubble.pilipili.common.util.LogUtil;
import com.bubble.pilipili.video.controller.VideoController;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Bubble
 * @date 2025/01/24 16:12
 */
@Aspect
@Slf4j
@Component
public class ControllerHandlingAspect {

    private Map<String, String> pathMap;

    @PostConstruct
    private void init(){
        pathMap = new HashMap<>();
        List<Class<? extends VideoController>> controllerClzList = new ArrayList<>();
        controllerClzList.add(VideoController.class);
//        Class<? extends VideoController> clz = VideoController.class;
        for (Class<?> clz : controllerClzList) {
            for (Method method : clz.getMethods()) {
                for (Class<?> anInterface : method.getReturnType().getInterfaces()) {
                    if (anInterface.equals(Response.class)) {
                        String path = AnnotationUtil.getApiPath(clz, method);
                        pathMap.put(method.getName(), path);
                        break;
                    }
                }
            }
        }
    }

    @Around("execution(* com.bubble.pilipili.video.controller.*.*(..))")
    public Object LoggingHandler(ProceedingJoinPoint joinPoint) throws Throwable {
        Class<?> clz = joinPoint.getTarget().getClass();
        String methodName = joinPoint.getSignature().getName();
        Object[] args = joinPoint.getArgs();

        long l1 = System.currentTimeMillis();
        try {
            Object rt = joinPoint.proceed();
            LogUtil.apiSuccessLog(clz, pathMap.get(methodName), args, rt, System.currentTimeMillis() - l1);
            return rt;
        } catch (Throwable throwable) {
            LogUtil.apiFailedLog(clz, pathMap.get(methodName), args, throwable.getMessage(), System.currentTimeMillis() - l1);
            throw throwable;
//            return SimpleResponse.error("Internal Server Error");
        }
    }

}
