package com.bubble.pilipili.auth.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * @author Bubble
 * @date 2024.12.09 16:31
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Validated
public class RoleMap {

    /**
     * 角色id
     */
    @NotNull
    @Valid
    private String id;

    /**
     * 角色名
     */
    @NotNull
    @Valid
    private String name;
}
