package com.bubble.pilipili.user.pojo.req;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 新增用户信息请求参数
 * @author liweixin@hcrc1.wecom.work
 * @date 2024/10/23
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SaveUserInfoRequest {

    /**
     * 昵称
     */
    private String nickname;
    /**
     * 密码（密文）
     */
    private String password;
    /**
     * 性别 0未知 1男 2女
     */
    private Integer gender;
    /**
     * 头像URL
     */
    private String avatarUrl;

    /**
     * 电子邮箱
     */
    private String email;

}
