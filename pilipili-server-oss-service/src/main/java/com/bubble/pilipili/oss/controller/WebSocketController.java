///*
// * Copyright (c) 2025. Bubble
// */
//
//package com.bubble.pilipili.oss.controller;
//
//import com.bubble.pilipili.oss.pojo.dto.TestMessageDTO;
//import com.bubble.pilipili.oss.pojo.req.TestMessageReq;
//import org.springframework.messaging.handler.annotation.MessageMapping;
//import org.springframework.messaging.handler.annotation.SendTo;
//import org.springframework.web.bind.annotation.RestController;
//
//import java.time.LocalDateTime;
//
///**
// * @author Bubble
// * @date 2025.03.21 21:14
// */
//@RestController
//public class WebSocketController {
//
//    private Integer count = 0;
//
//    @MessageMapping("/hello")
//    @SendTo("/topic/greetings")
//    public TestMessageDTO test(TestMessageReq req) {
//        count++;
//        String msg = "Serve reply: " + req.getMessage();
//        return new TestMessageDTO(msg, count, LocalDateTime.now());
//    }
//}
