package com.bubble.pilipili.user.pojo.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author liweixin@hcrc1.wecom.work
 * @date 2024/10/23
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SaveUserInfoDTO implements Serializable {

    /**
     * 是否成功
     */
    private Boolean success;

    /**
     * 注册成功生成的UID
     */
    private Integer uid;
}
