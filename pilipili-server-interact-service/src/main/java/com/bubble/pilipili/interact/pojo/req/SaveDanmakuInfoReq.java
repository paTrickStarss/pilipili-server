/*
 * Copyright (c) 2025. Bubble
 */

package com.bubble.pilipili.interact.pojo.req;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * @author Bubble
 * @date 2025.03.06 13:53
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SaveDanmakuInfoReq implements Serializable {

    /**
     * 所在视频ID
     */
    @NotBlank(message = "视频ID不能为空")
    private Integer vid;
    /**
     * 发送用户ID
     */
    @NotBlank(message = "发送用户ID不能为空")
    private Integer uid;
    /**
     * 弹幕出现时刻（秒）
     */
    @NotBlank(message = "弹幕出现时刻不能为空")
    private Integer timing;
    /**
     * 弹幕内容
     */
    @NotBlank(message = "弹幕内容不能为空")
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
}
