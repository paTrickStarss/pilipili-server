/*
 * Copyright (c) 2025. Bubble
 */

package com.bubble.pilipili.common.util.function;

import java.util.Objects;

/**
 * 接受三个参数的消费方法
 * @author Bubble
 * @date 2025.03.16 17:11
 */
@FunctionalInterface
public interface TriConsumer<T, P, K> {

    /**
     * 传递三个参数执行方法
     * @param t
     * @param p
     * @param k
     */
    void accept(T t, P p, K k);


    /**
     * 顺序拼接消费方法
     * @param after
     * @return
     */
    default TriConsumer<T, P, K> andThen(TriConsumer<? super T, ? super P, ? super K> after) {
        Objects.requireNonNull(after);

        return (t, p, k) -> {
            accept(t, p, k);
            after.accept(t, p, k);
        };
    }
}
