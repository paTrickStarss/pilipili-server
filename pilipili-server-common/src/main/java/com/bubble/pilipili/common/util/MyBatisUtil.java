/*
 * Copyright (c) 2025. Bubble
 */

package com.bubble.pilipili.common.util;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.core.toolkit.LambdaUtils;
import com.baomidou.mybatisplus.core.toolkit.support.LambdaMeta;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.bubble.pilipili.common.exception.UtilityException;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.property.PropertyNamer;

import java.beans.PropertyDescriptor;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

/**
 * @author Bubble
 * @date 2025.03.13 18:10
 */
@Slf4j
public class MyBatisUtil {

    /**
     * 解析Lambda得字段名（支持TableField、TableId注解）
     * @param getterFunc 字段的getter函数，Lambda表达式
     * @return
     * @param <T>
     */
    public static <T> String getFieldName(SFunction<T, ?> getterFunc) {
        LambdaMeta meta = LambdaUtils.extract(getterFunc);
        String implMethodName = meta.getImplMethodName();
        // 这是MyBatis默认解析出来的字段名
        String fieldName = PropertyNamer.methodToProperty(implMethodName);

        Class<?> instantiatedClass = meta.getInstantiatedClass();
        Field field;
        try {
            field = instantiatedClass.getDeclaredField(fieldName);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }

        List<Class<? extends Annotation>> annotationList = Arrays.asList(TableField.class, TableId.class);
        String value;
        for (Class<? extends Annotation> annotationClz : annotationList) {
            value = AnnotationUtil.getFieldAnnotationValue(field, annotationClz, "value");
            if (value != null) {
                return value;
            }
        }
        // 没有找到注解的字段名则返回默认字段名
        return fieldName;
    }

    /**
     * 更新实体类指定字段值
     * @param entity 实体对象
     * @param getterFunc Lambda表达式指定字段
     * @param value 字段值
     * @param <T> 实体类
     * @param <V> 字段值类型
     */
    public static <T, V> void updateFieldValue(T entity, SFunction<T, V> getterFunc, V value) {
        Class<?> clz = entity.getClass();

        LambdaMeta meta = LambdaUtils.extract(getterFunc);
        String implMethodName = meta.getImplMethodName();
        // 这是MyBatis默认解析出来的字段名
        String fieldName = PropertyNamer.methodToProperty(implMethodName);

        // 这个instantiatedClass应该与上面的clz是一样的
//        Class<?> instantiatedClass = meta.getInstantiatedClass();
//        Field field;
//        try {
//            field = instantiatedClass.getDeclaredField(fieldName);
//        } catch (Exception e) {
//            log.warn("字段对象获取异常");
//            throw new UtilityException(e.getMessage());
//        }

        try {
            Method setter = new PropertyDescriptor(fieldName, clz).getWriteMethod();
            setter.invoke(entity, value);

        } catch (Exception e) {
            log.warn("字段赋值异常");
            throw new UtilityException(e.getMessage());
        }
    }
}
