/*
 * Copyright (c) 2025. Bubble
 */

package com.bubble.pilipili.common.util;

import com.bubble.pilipili.common.http.Controller;
import com.bubble.pilipili.common.http.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AssignableTypeFilter;

import java.lang.reflect.Method;
import java.util.*;

/**
 * 类对象扫描器
 * @author Bubble
 * @date 2025.03.02 21:08
 */
@Slf4j
public class ClassScanner {

    /**
     * 扫描指定包下所有Class
     * @param packageName 包名
     * @return
     */
    public static List<Class<?>> scanPackage(String packageName) {
        return scanPackage(packageName, Object.class);
    }

    /**
     * 扫描指定包下所有指定类型Class
     * @param packageName 包名
     * @param baseClass 指定类型Class
     * @return
     */
    public static <T> List<Class<? extends T>> scanPackage(String packageName, Class<T> baseClass) {
        getScanner().resetFilters(false);
        getScanner().addIncludeFilter(new AssignableTypeFilter(baseClass));
        return getPackageClzList(getScanner().findCandidateComponents(packageName));
    }

    /**
     * 扫描指定包下所有Controller API路径
     * @param packageName 包名
     * @return Map(key:methodName, value:path)
     */
    public static Map<String, String> scanControllerPath(String packageName) {
        Map<String, String> pathMap = new HashMap<>();
        List<Class<? extends Controller>> controllerClzList = scanPackage(packageName, Controller.class);
        for (Class<?> clz : controllerClzList) {
            String clzName = clz.getSimpleName();
            for (Method method : clz.getMethods()) {
                for (Class<?> anInterface : method.getReturnType().getInterfaces()) {
                    if (anInterface.equals(Response.class)) {
                        String path = AnnotationUtil.getApiPath(clz, method);
                        pathMap.put(String.join(".", clzName, method.getName()), path);
                        break;
                    }
                }
            }
        }

        return pathMap;
    }

    private static class LazyHolder {
        private static final ClassPathScanningCandidateComponentProvider scanner =
                new ClassPathScanningCandidateComponentProvider(false);
    }
    private static ClassPathScanningCandidateComponentProvider getScanner() {
        return LazyHolder.scanner;
    }

    private static <T> List<Class<? extends T>> getPackageClzList(Set<BeanDefinition> beanDefinitions) {
        List<Class<? extends T>> clzList = new ArrayList<>();
        for (BeanDefinition beanDefinition : beanDefinitions) {
            String beanClassName = beanDefinition.getBeanClassName();
            try {
                Class<? extends T> aClass = (Class<? extends T>) Class.forName(beanClassName);
                clzList.add(aClass);
            } catch (ClassNotFoundException ex) {
                log.error("Class Loading Error:\n{}", ex.getMessage());
            }
        }

        return clzList;
    }


}
