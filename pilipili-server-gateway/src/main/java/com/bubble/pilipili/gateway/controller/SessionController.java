/*
 * Copyright (c) 2024. Bubble
 */

package com.bubble.pilipili.gateway.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author liweixin@hcrc1.wecom.work
 * @date 2024/10/14
 */
@RestController
@RequestMapping("/session")
public class SessionController {


    @GetMapping("/health")
    public String health() {
        return "OK";
    }
}
