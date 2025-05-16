/*
 * Copyright (c) 2025. Bubble
 */

package com.bubble.pilipili.common.component;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 实体类转换器
 * @author Bubble
 * @date 2025.03.17 16:53
 */
@Slf4j
@Component
public class EntityConverter {

    private final Map<String, Map<String, Field>> fieldCache = new HashMap<>();

    private Map<String, Field> getFieldCacheMap(Class<?> clz) {
        String clzName = clz.getName();
        return fieldCache.computeIfAbsent(clzName,
                k -> Arrays.stream(getDeclaredFieldsWithSuper(clz))
                        .collect(Collectors.toMap(Field::getName, Function.identity()))
        );
    }

    /**
     * 获取声明字段（包含父类）
     * @param clz
     * @return
     * @param <T>
     */
    private <T> Field[] getDeclaredFieldsWithSuper(Class<T> clz) {
        Field[] declaredFields = clz.getDeclaredFields();
        Class<? super T> superclass = clz.getSuperclass();
        if (superclass != null) {
            Field[] superFields = superclass.getDeclaredFields();
            return Stream.concat(Arrays.stream(superFields), Arrays.stream(declaredFields)).toArray(Field[]::new);
        }
        return declaredFields;
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
            result = resultClz.getConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException |
                 NoSuchMethodException | InvocationTargetException e) {
            log.error("创建实例异常！{}", e.getMessage());
            throw new RuntimeException(e);
        }
        Class<?> fromClz = from.getClass();
        Map<String, Field> resultFieldMap = getFieldCacheMap(resultClz);
        Map<String, Field> fromFieldMap = getFieldCacheMap(fromClz);

        doWriteFiledValue(fromFieldMap, resultFieldMap, fromClz, resultClz, from, result);

        return result;
    }

    public <F, T> List<T> copyFieldValueList(List<F> fromList, Class<T> resultClz) {
        if (fromList == null) return null;

        List<T> resultList = new ArrayList<>();
        if (fromList.isEmpty()) return resultList;

        Class<?> fromClz = fromList.get(0).getClass();
        Map<String, Field> resultFieldMap = getFieldCacheMap(resultClz);
        Map<String, Field> fromFieldMap = getFieldCacheMap(fromClz);

        fromList.forEach(from -> {
            if (from == null) return;
            T result;
            try {
                result = resultClz.getConstructor().newInstance();
            } catch (InstantiationException | IllegalAccessException |
                     InvocationTargetException | NoSuchMethodException e) {
                log.error("创建实例异常！{}", e.getMessage());
                throw new RuntimeException(e);
            }
            doWriteFiledValue(
                    fromFieldMap, resultFieldMap,
                    fromClz, resultClz, from, result
            );
            resultList.add(result);
        });

        return resultList;
    }

    /**
     * 合并更新的字段值
     * @param origin 更新前原数据
     * @param update 待更新数据
     * @return 合并更新字段后的完整数据
     * @param <T> 数据类型
     */
    public <T> T copyUpdateFieldValue(T origin, T update) {
        Class<?> clz = origin.getClass();
        Map<String, Field> fieldMap = getFieldCacheMap(clz);
        fieldMap.forEach((name, field) -> {
            try {
                Method readMethod = new PropertyDescriptor(field.getName(), clz).getReadMethod();
                Object fieldValue = readMethod.invoke(update);
                if (fieldValue != null) {
                    Method writeMethod = new PropertyDescriptor(field.getName(), clz).getWriteMethod();
                    writeMethod.invoke(origin, fieldValue);
                }
            } catch (IntrospectionException | InvocationTargetException | IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        });

        return origin;
    }


    private <F, T> void doWriteFiledValue(
            Map<String, Field> fromFieldMap,
            Map<String, Field> resultFieldMap,
            Class<?> fromClz,
            Class<T> resultClz,
            F from,
            T result

    ) {
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
    }
}
