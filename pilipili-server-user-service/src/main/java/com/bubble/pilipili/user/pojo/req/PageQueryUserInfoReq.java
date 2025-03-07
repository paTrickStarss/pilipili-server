/*
 * Copyright (c) 2025. Bubble
 */

package com.bubble.pilipili.user.pojo.req;

import com.bubble.pilipili.common.http.PageQueryReq;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @author Bubble
 * @date 2025.03.07 15:21
 */
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PageQueryUserInfoReq extends PageQueryReq {

    private Integer uid;
    private Boolean special;
}
