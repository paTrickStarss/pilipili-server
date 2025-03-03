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
 * @date 2025.03.03 21:39
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class QueryDynamicInfoReq implements Serializable {

    @NotBlank(message = "请传入动态ID（did）")
    private Integer did;
}
