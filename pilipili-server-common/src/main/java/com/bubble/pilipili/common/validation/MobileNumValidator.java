/*
 * Copyright (c) 2025. Bubble
 */

package com.bubble.pilipili.common.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * @author Bubble
 * @date 2025.03.03 21:25
 */
public class MobileNumValidator implements ConstraintValidator<MobileNum, String> {

    /**
     * @param s
     * @param constraintValidatorContext
     * @return
     */
    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        return s != null && s.matches("^1[3-9]\\d{9}$");
    }
}
