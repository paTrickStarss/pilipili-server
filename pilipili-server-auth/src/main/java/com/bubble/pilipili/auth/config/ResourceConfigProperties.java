/*
 * Copyright (c) 2024-2025. Bubble
 */

package com.bubble.pilipili.auth.config;

import com.bubble.pilipili.auth.entity.ResourceMap;
import com.bubble.pilipili.auth.entity.RoleMap;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

/**
 * @author liweixin@hcrc1.wecom.work
 * @date 2024/10/25
 */
@ConfigurationProperties(prefix = "resource", ignoreInvalidFields = true)
@Validated
@Data
public class ResourceConfigProperties {

    /**
     * 资源访问权限映射
     */
    @NotNull
    @Valid
    private List<ResourceMap> pathRoleMap = new ArrayList<>();

    /**
     * 角色列表
     */
    @NotNull
    @Valid
    private List<RoleMap> roleMap = new ArrayList<>();

}
