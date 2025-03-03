/*
 * Copyright (c) 2025. Bubble
 */

package com.bubble.pilipili.interact.pojo.req;

import com.bubble.pilipili.common.http.PageQueryReq;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;

/**
 * @author Bubble
 * @date 2025.03.01 16:35
 */
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PageQueryDynamicInfoReq extends PageQueryReq {

    @NotBlank(message = "uid不能为空")
    @Positive(message = "uid必须大于0")
    private Integer uid;

}
