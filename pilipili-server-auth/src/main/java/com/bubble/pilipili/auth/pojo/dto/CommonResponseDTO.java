/*
 * Copyright (c) 2024. Bubble
 */

package com.bubble.pilipili.auth.pojo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author liweixin@hcrc1.wecom.work
 * @date 2024/12/25
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommonResponseDTO {

    private Boolean success;
    private String msg;
}
