/*
 * Copyright (c) 2025. Bubble
 */

package com.bubble.pilipili.interact.pojo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 弹幕信息实体类
 * @author Bubble
 * @date 2025.02.28 21:40
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("danmaku_info")
public class DanmakuInfo {

    /**
     * 弹幕ID
     */
    @TableId(type = IdType.AUTO)
    private Integer danmakuId;
    /**
     * 所在视频ID
     */
    private Integer vid;
    /**
     * 发送用户ID
     */
    private Integer uid;
    /**
     * 弹幕出现时刻（秒）
     */
    private Integer timing;
    /**
     * 弹幕内容
     */
    private String content;
    /**
     * 字体大小 0小 1中 2大
     */
    private Integer fontSize;
    /**
     * 弹幕类型 0滚动 1顶部 2底部
     */
    private Integer danmakuType;
    /**
     * 弹幕颜色
     */
    private String color;
    /**
     * 发送时间
     */
    private LocalDateTime createTime;
    /**
     * 删除
     */
    private Integer rm;
}
