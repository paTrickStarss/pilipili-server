package com.bubble.pilipili.auth.config;

import com.bubble.pilipili.auth.entity.ResourceMap;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

/**
 * @author liweixin@hcrc1.wecom.work
 * @date 2024/10/25
 */
@Component
@ConfigurationProperties(prefix = "resource", ignoreInvalidFields = true)
@Validated
@Data
public class ResourceConfigProperties {

    /**
     * 资源访问权限映射
     */
    @NotNull
    @Valid
    private List<ResourceMap> maps = new ArrayList<>();

}
