/*
 * Copyright (c) 2025. Bubble
 */

package com.bubble.pilipili.common.util;

/**
 * @author Bubble
 * @date 2025.03.10 17:39
 */
public class IntegerUtil {

    /**
     * 取反值 <br>
     * 1 -> 0；<br>
     * 0,null -> 1;
     * @param value
     * @return
     */
    public static Integer getOpposite(Integer value) {
        if (value == null || value == 0) {
            return 1;
        }
        return 0;
    }
}
