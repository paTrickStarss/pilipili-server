/*
 * Copyright (c) 2025. Bubble
 */

package com.bubble.pilipili.common.util.quartz;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author Bubble
 * @date 2025/01/27 23:10
 */
public class TestMain {

    public static void main(String[] args) {
        // 线程不安全的
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        String now = sdf.format(new Date());

        // 线程安全的
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss:SSS");
        String now = LocalDateTime.now().format(formatter);

        ScheduleService scheduleService = new ScheduleService();
//        scheduleService.schedule(() -> {
//            System.out.println(LocalDateTime.now().format(formatter) + " This is a demo thread with 200ms delay");
//        }, 200);
        scheduleService.schedule(() -> {
            System.out.println(LocalDateTime.now().format(formatter) + " This is a demo thread with 1000ms delay----");
        }, 1000);
    }
}
