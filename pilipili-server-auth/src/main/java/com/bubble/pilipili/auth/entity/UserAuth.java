package com.bubble.pilipili.auth.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author liweixin@hcrc1.wecom.work
 * @date 2024/10/24
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("user_info")
public class UserAuth {

    @TableId("uid")
    private Integer username;

    @TableField("password")
    private String password;

    @TableField("role")
    private String role;
}
