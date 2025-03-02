/*
 * Copyright (c) 2025. Bubble
 */

package com.bubble.pilipili.interact.config;

import com.bubble.pilipili.common.util.AspectHandlerAction;
import com.bubble.pilipili.common.util.ClassScanner;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.util.Map;

/**
 * @author Bubble
 * @date 2025.03.02 22:07
 */
@Aspect
@Slf4j
@Configuration
public class InteractAspectHandler {

    private Map<String, String> pathMap;

    @PostConstruct
    private void init(){
        pathMap = ClassScanner.scanControllerPath("com.bubble.pilipili.interact.controller");
    }

    @Around("execution(* com.bubble.pilipili.common.http.Controller+.*(..))")
    public Object LoggingHandler(ProceedingJoinPoint joinPoint) throws Throwable {
        return AspectHandlerAction.controllerLoggingAction(joinPoint, pathMap);
    }
}
