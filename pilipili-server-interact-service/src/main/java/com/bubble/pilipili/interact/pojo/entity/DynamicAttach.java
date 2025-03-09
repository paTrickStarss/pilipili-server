/*
 * Copyright (c) 2025. Bubble
 */

package com.bubble.pilipili.interact.pojo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 动态附件信息实体类
 * @author Bubble
 * @date 2025.03.01 14:42
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("dynamic_attach")
public class DynamicAttach {


    /**
     * 附件ID
     */
    @TableId(type = IdType.AUTO)
    private Integer attachId;
    /**
     * 动态ID
     */
    private Integer did;
    /**
     * 附件UUID
     */
    @TableField(value = "attach_uuid")
    private String attachUUID;
    /**
     * 附件类型 0图片 1视频
     */
    private Integer attachType;
    /**
     * 附件链接（OSS相对路径）
     */
    private String attachUrl;
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
}
