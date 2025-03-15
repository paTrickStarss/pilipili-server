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
 * @date 2025.03.15 13:58
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SendCommentStatsReq implements Serializable {

    private Integer cid;
    private Long favorCount;
    private Long dewCount;
}
