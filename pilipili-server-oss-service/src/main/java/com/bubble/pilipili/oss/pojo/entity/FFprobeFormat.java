/*
 * Copyright (c) 2025. Bubble
 */

package com.bubble.pilipili.oss.pojo.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Bubble
 * @date 2025.04.01 15:36
 */
@Data
@NoArgsConstructor
public class FFprobeFormat {
    
    private String filename;
    private Integer nb_streams;
    private Integer nb_programs;
    private Integer nb_stream_groups;
    private String format_name;
    private String format_long_name;
    private String start_time;
    private String duration;
    private String size;
    private String bit_rate;
    private Integer probe_score;
}
