/*
 * Copyright (c) 2025. Bubble
 */

package com.bubble.pilipili.common.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Bubble
 * @date 2025.03.03 19:57
 */
@ConfigurationProperties(prefix = "security-config", ignoreInvalidFields = true)
@Validated
@Data
public class SecurityConfigProperties {

    /**
     * Security鉴权路径白名单
     */
    @NotNull @Valid
    private List<String> ignoreUrlList = new ArrayList<>();
}
