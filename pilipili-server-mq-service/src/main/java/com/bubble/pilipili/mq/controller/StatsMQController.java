/*
 * Copyright (c) 2025. Bubble
 */

package com.bubble.pilipili.mq.controller;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Bubble
 * @date 2025.03.14 18:58
 */
@RestController
@RequestMapping("/mq/stats")
public class StatsMQController {

    @Autowired
    private RabbitTemplate rabbitTemplate;
}
