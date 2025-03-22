/*
 * Copyright (c) 2025. Bubble
 */

package com.bubble.pilipili.common.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author Bubble
 * @date 2025.03.22 23:19
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class JwtPayload {

    private String username;
    private String jti;
    private List<String> authorities;
    private Long exp;
    private List<String> scope;
    private String client_id;
    private String user_name;
}
