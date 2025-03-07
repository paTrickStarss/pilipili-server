/*
 * Copyright (c) 2024-2025. Bubble
 */

package com.bubble.pilipili.user.pojo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 查询用户信息返回参数
 * @author liweixin@hcrc1.wecom.work
 * @date 2024/10/23
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class QueryUserInfoDTO implements Serializable {

    /**
     * 用户ID
     */
    private Integer uid;
    /**
     * UUID
     */
    private String uuid;
    /**
     * 昵称
     */
    private String nickname;

    /**
     * 性别 0未知 1男 2女
     */
    private Integer gender;
    /**
     * 头像URL
     */
    private String avatarUrl;
    /**
     * 背景图URL
     */
    private String backgroundUrl;
    /**
     * 电子邮箱
     */
    private String email;
    /**
     * 个人介绍
     */
    private String description;
    /**
     * 会员状态：0普通用户 1月度大会员 2季度大会员 3年度大会员 4云视听月度大会员 5云视听季度大会员 6云视听年度大会员
     */
    private Integer vipStatus;
    /**
     * 小闪电认证：0无认证 1名人认证 2机构认证
     */
    private Integer authority;
    /**
     * 小闪电认证说明
     */
    private String authorityDesc;
    /**
     * 账号状态：0已注销 1正常 2封禁中
     */
    private Integer validStatus;
    /**
     * 账号类型：0普通账号 1管理员账号
     */
    private Integer role;
    /**
     * 等级经验值
     */
    private Integer exp;
    /**
     * 硬币（普通代币）
     */
    private Integer hCoin;
    /**
     * P币（充值代币）
     */
    private Integer pCoin;
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
    /**
     * 最后一次更新时间
     */
    private LocalDateTime updateTime;

    /**
     * 用户等级 0-6 （计算得出）
     */
    private Integer level;

    /**
     * 关注数
     */
    private Long followerCount = 0L;
    /**
     * 粉丝数
     */
    private Long fansCount = 0L;
}
