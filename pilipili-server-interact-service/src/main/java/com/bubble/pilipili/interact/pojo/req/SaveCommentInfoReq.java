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
 * @date 2025.03.05 16:39
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SaveCommentInfoReq implements Serializable {

    /**
     * 发送用户ID
     */
    @NotBlank(message = "发送用户ID不能为空")
    private Integer uid;
    /**
     * 回复评论ID（非回复时为空）
     */
    private Integer parentId;
    /**
     * 回复评论根ID（非回复时为空）
     */
    private Integer parentRootId;
    /**
     * 评论对象类型 1视频 2动态 3评论（回复）
     */
    @NotBlank(message = "评论对象类型不能为空")
    private Integer relaType;
    /**
     * 评论对象ID
     */
    @NotBlank(message = "评论对象ID不能为空")
    private Integer relaId;
    /**
     * 评论内容
     */
    @NotBlank(message = "评论内容不能为空")
    private String content;
}
