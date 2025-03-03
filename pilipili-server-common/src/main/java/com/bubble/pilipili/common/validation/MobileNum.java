/*
 * Copyright (c) 2025. Bubble
 */

package com.bubble.pilipili.common.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

/**
 * @author Bubble
 * @date 2025.03.03 21:24
 */

@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = MobileNumValidator.class)
public @interface MobileNum {

    String message() default "手机号码格式不正确";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
