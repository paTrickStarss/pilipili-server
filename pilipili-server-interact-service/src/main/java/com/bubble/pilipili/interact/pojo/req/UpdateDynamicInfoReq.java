/*
 * Copyright (c) 2025. Bubble
 */

package com.bubble.pilipili.interact.pojo.req;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.List;

/**
 * @author Bubble
 * @date 2025.03.18 20:57
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateDynamicInfoReq implements Serializable {

    /**
     * 动态ID
     */
    @NotBlank(message = "动态ID不能为空")
    private Integer did;
    /**
     * 内容
     */
    @NotBlank(message = "动态内容不能为空")
    private String content;
    /**
     * 是否为转发动态
     */
    @Min(value = 0, message = "该字段值为0或1")
    @Max(value = 1, message = "该字段值为0或1")
    private Integer isRepost;
    /**
     * 引用类型 1视频 2动态
     */
    @Min(value = 1, message = "该字段值为1或2 其中1视频 2动态")
    @Max(value = 2, message = "该字段值为1或2 其中1视频 2动态")
    private Integer relaType;
    /**
     * 引用对象ID
     */
    private Integer relaId;

    /**
     * 附件
     */
    List<SaveDynamicAttachReq> attachList;
    /**
     * 删除附件
     */
    List<SaveDynamicAttachReq> attachRemoveList;
}
