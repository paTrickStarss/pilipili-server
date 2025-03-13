/*
 * Copyright (c) 2025. Bubble
 */

package com.bubble.pilipili.common.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Bubble
 * @date 2025/01/24 16:32
 */
@Slf4j
public class AnnotationUtil {

    /**
     * 获取字段注解参数值
     * @param field
     * @param annotationClass
     * @param annotationFieldName
     * @return
     */
    public static String getFieldAnnotationValue(Field field, Class<? extends Annotation> annotationClass, String annotationFieldName) {
        try {
            Annotation annotation = field.getAnnotation(annotationClass);
            return getAnnotationFieldValue(annotation, annotationFieldName);

        } catch (NoSuchMethodException | IllegalAccessException e) {
            log.error("getMethodAnnotationValue error: {}", e.getMessage(), e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    /**
     * 获取方法注解参数值
     * @param method 所在方法
     * @param annotationClass 注解类
     * @param annotationFieldName 需要获取的参数名
     * @return 目标注解参数字段值
     */
    public static String getMethodAnnotationValue(Method method, Class<? extends Annotation> annotationClass, String annotationFieldName) {
        try {
            Annotation annotation = method.getAnnotation(annotationClass);
            return getAnnotationFieldValue(annotation, annotationFieldName);

        } catch (NoSuchMethodException | IllegalAccessException e) {
            log.error("getMethodAnnotationValue error: {}", e.getMessage(), e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    /**
     * 获取类注解参数值
     * @param clz 所在类
     * @param annotationClass 注解类
     * @param annotationFieldName 需要获取的参数名
     * @return 目标注解参数字段值
     */
    public static String getClassAnnotationValue(Class<?> clz, Class<? extends Annotation> annotationClass, String annotationFieldName) {
        try {
            Annotation annotation = clz.getAnnotation(annotationClass);
            return getAnnotationFieldValue(annotation, annotationFieldName);

        } catch (NoSuchMethodException | IllegalAccessException e) {
            log.error("getClassAnnotationValue error: {}", e.getMessage(), e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    /**
     * 获取RequestMapping注解path字段值
     * @param clz 当前类
     * @param method 当前方法
     * @return path字段值
     */
    public static String getApiPath(Class<?> clz, Method method) {
        List<Class<? extends Annotation>> classlist = new ArrayList<>();
        classlist.add(RequestMapping.class);
        classlist.add(GetMapping.class);
        classlist.add(PostMapping.class);
        classlist.add(PutMapping.class);
        classlist.add(PatchMapping.class);
        classlist.add(DeleteMapping.class);

        String path = null;
        for (Class<? extends Annotation> annotationClz : classlist) {
            path = getMethodAnnotationValue(method, annotationClz, "value");
            if (path != null) {
                break;
            }
        }
        String rtPath = getClassAnnotationValue(clz, RequestMapping.class, "value");
        return rtPath + path;
    }


    private static String getAnnotationFieldValue(Annotation annotation, String annotationFieldName) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        if (annotation == null) {
            return null;
        }
        Object invoke = annotation.annotationType().getMethod(annotationFieldName).invoke(annotation);
        if (invoke == null) {
            return null;
        }
        if (invoke instanceof String) {
            return (String) invoke;
        }
        if (invoke instanceof Object[]) {
            String[] values = ((String[]) invoke);
            return values.length > 0 ? values[0] : null;
        }
        return null;
    }
}
