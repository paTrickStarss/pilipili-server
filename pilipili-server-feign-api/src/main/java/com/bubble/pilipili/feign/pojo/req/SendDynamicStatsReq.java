/*
 * Copyright (c) 2025. Bubble
 */

package com.bubble.pilipili.feign.pojo.req;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author Bubble
 * @date 2025.03.15 14:56
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SendDynamicStatsReq implements Serializable {
    private Integer did;
    private Long favorCount;
    private Long commentCount;
    private Long repostCount;
}
