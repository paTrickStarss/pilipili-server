/*
 * Copyright (c) 2024. Bubble
 */

package com.bubble.pilipili.auth.config;

import com.bubble.pilipili.common.config.PasswordEncoderConfig;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @author liweixin@hcrc1.wecom.work
 * @date 2024/12/31
 */
@Configuration
@Import(PasswordEncoderConfig.class)
public class AuthPasswordEncoderConfig {
}
