/*
 * Copyright (c) 2025. Bubble
 */

package com.bubble.pilipili.video.service.impl;

import com.bubble.pilipili.video.pojo.req.CreateVideoInfoReq;
import com.bubble.pilipili.video.service.VideoInfoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;
/**
 * @author Bubble
 * @date 2025.05.06 21:37
 */
@SpringBootTest
class VideoInfoServiceImplTest {

    @Autowired
    VideoInfoService videoInfoService;

    @Test
    void saveVideoInfo() {
        CreateVideoInfoReq req = new CreateVideoInfoReq();
        req.setUid(1005);
        req.setTitle("test title");
        req.setDescription("test description");

        Boolean b = videoInfoService.saveVideoInfo(req);
        assertTrue(b);
    }
}