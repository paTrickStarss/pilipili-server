/*
 * Copyright (c) 2025. Bubble
 */

package com.bubble.pilipili.video.pojo.param;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Bubble
 * @date 2025/01/20 19:03
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class QueryVideoInfoParam {

    private String keyword;
    private String title;
    private String tag;
    private String publishDateStart;
    private String publishDateEnd;

}
