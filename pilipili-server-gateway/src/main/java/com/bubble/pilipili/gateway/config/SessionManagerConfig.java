/*
 * Copyright (c) 2024. Bubble
 */

package com.bubble.pilipili.gateway.config;

import com.bubble.pilipili.common.component.SessionManager;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @author liweixin@hcrc1.wecom.work
 * @date 2024/12/31
 */
@Configuration
@Import(SessionManager.class)
public class SessionManagerConfig {
}
