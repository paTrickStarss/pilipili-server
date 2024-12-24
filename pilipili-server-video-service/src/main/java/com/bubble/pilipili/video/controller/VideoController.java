package com.bubble.pilipili.video.controller;

import com.bubble.pilipili.common.http.SimpleResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author liweixin@hcrc1.wecom.work
 * @date 2024/10/23
 */
@RestController
@RequestMapping("/api/video")
public class VideoController {


    @GetMapping("/test")
    public SimpleResponse<String> test() {
        String a = "username";
        return SimpleResponse.success("Hello World");
    }
}
