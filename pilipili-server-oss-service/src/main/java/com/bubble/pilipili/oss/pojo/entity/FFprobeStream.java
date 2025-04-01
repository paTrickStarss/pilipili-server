/*
 * Copyright (c) 2025. Bubble
 */

package com.bubble.pilipili.oss.pojo.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Bubble
 * @date 2025.04.01 15:12
 */
@Data
@NoArgsConstructor
public class FFprobeStream {

    /**
     * 流序号：<br>
     * 0 - Video <br>
     * 1 - Audio
     */
    private Integer index;
    private String codec_name;
    private String codec_long_name;
    private String profile;
    private String codec_type;
    private String codec_tag_string;
    private String codec_tag;
    private Integer width;
    private Integer height;
    private Integer coded_width;
    private Integer coded_height;
    private Integer has_b_frames;
    private String sample_aspect_ratio;
    private String display_aspect_ratio;
    private String pix_fmt;
    private Integer level;
    private String color_range;
    private String color_space;
    private String color_transfer;
    private String color_primaries;
    private String chroma_location;
    private String field_order;
    private Integer refs;
    private String is_avc;
    private String nal_length_size;
    private String id;
    private String r_frame_rate;
    private String avg_frame_rate;
    private String time_base;
    private Integer start_pts;
    private String start_time;
    private Long duration_ts;
    private String duration;
    private String bit_rate;
    private String bits_per_raw_sample;
    private String nb_frames;
    private Integer extradata_size;

    // Audio 特有的属性
    private String sample_fmt;
    private String sample_rate;
    private Integer channels;
    private String channel_layout;
    private Integer bits_per_sample;
    private Integer initial_padding;

    private FFprobeStreamDisposition disposition;
    



}
