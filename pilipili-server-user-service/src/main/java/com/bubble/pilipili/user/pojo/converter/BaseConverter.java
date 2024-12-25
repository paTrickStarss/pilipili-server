/*
 * Copyright (c) 2024. Bubble
 */

package com.bubble.pilipili.user.pojo.converter;

import lombok.extern.slf4j.Slf4j;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 值类转化器基类
 * @author liweixin@hcrc1.wecom.work
 * @date 2024/12/24
 */
@Slf4j
public class BaseConverter {

    protected BaseConverter() {}
    private static class lazyHolder {
        private static final BaseConverter INSTANCE = new BaseConverter();
        private static final Map<String, Map<String, Field>> fieldCache = new HashMap<>();
    }
    public static BaseConverter getInstance() {
        return lazyHolder.INSTANCE;
    }
    private Map<String, Field> getFieldCacheMap(Class<?> clz) {
        String clzName = clz.getName();
        return lazyHolder.fieldCache.computeIfAbsent(clzName,
                k -> Arrays.stream(clz.getDeclaredFields()).collect(Collectors.toMap(Field::getName, field -> field)));
    }

    /**
     * 复制相同字段的值
     * @param from 源对象
     * @param resultClz 目标对象
     * @return 字段赋值后的目标对象
     * @param <F> 源对象类型
     * @param <T> 目标对象类型
     */
    public <F, T> T copyFieldValue(F from, Class<T> resultClz) {
        if (from == null) return null;

        T result;
        try {
            result = resultClz.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            log.error("创建实例异常！{}", e.getMessage());
            throw new RuntimeException(e);
        }
        Class<?> fromClz = from.getClass();
        Map<String, Field> resultFieldMap = getFieldCacheMap(resultClz);
        Map<String, Field> fromFieldMap = getFieldCacheMap(fromClz);

        fromFieldMap.forEach((name, fromField) -> {
            // 包含同名字段
            if (resultFieldMap.containsKey(name)) {
                Field curField = resultFieldMap.get(fromField.getName());
                // 包含同名同类型字段
                if (curField.getType().equals(fromField.getType())) {
                    try {
                        // 赋值
                        Method writeMethod = new PropertyDescriptor(curField.getName(), resultClz).getWriteMethod();
                        Method readMethod = new PropertyDescriptor(fromField.getName(), fromClz).getReadMethod();
                        Object fieldValue = readMethod.invoke(from);
                        writeMethod.invoke(result, fieldValue);

                    } catch (NullPointerException e) {
                        log.error("类字段getter/setter方法为空！{}", e.getMessage());
                    } catch (IntrospectionException | InvocationTargetException | IllegalAccessException e) {
                        log.error("类字段getter/setter方法调用异常！ {}", e.getMessage());
                    }
                }
            }
        });

        return result;
    }
}
