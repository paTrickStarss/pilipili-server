package com.bubble.pilipili.auth.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author liweixin@hcrc1.wecom.work
 * @date 2024/10/25
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Validated
public class ResourceMap {

    /**
     * 资源请求路径
     */
    @NotNull
    @Valid
    private String path;
    /**
     * 允许访问角色列表
     */
    @NotNull
    @Valid
    private List<String> role;
}
