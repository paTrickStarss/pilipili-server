/*
 * Copyright (c) 2025. Bubble
 */

package com.bubble.pilipili.oss.pojo.entity;

import com.alibaba.fastjson2.annotation.JSONField;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Bubble
 * @date 2025.04.01 15:19
 */
@Data
@NoArgsConstructor
public class FFprobeStreamDisposition {

    @JSONField(name = "default")
    private Integer _default;
    private Integer dub;
    private Integer original;
    private Integer comment;
    private Integer lyrics;
    private Integer karaoke;
    private Integer forced;
    private Integer hearing_impaired;
    private Integer visual_impaired;
    private Integer clean_effects;
    private Integer attached_pic;
    private Integer timed_thumbnails;
    private Integer non_diegetic;
    private Integer captions;
    private Integer descriptions;
    private Integer metadata;
    private Integer dependent;
    private Integer still_image;
    private Integer multilayer;
}
