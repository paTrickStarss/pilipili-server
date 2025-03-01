/*
 * Copyright (c) 2025. Bubble
 */

package com.bubble.pilipili.video.config;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.context.annotation.Configuration;

/**
 * @author Bubble
 * @date 2025/01/21 16:24
 */
@Slf4j
@Aspect
@Configuration
public class ExceptionHandlingAspect {

    @Around("execution(* com.bubble.pilipili.video.service.*.*(..))")
    public Object handleServiceException(ProceedingJoinPoint joinPoint) throws Throwable {
        try {
            return joinPoint.proceed();
        } catch (Exception e) {
            log.error("Service section Error");
            throw e;
        }
    }
}
