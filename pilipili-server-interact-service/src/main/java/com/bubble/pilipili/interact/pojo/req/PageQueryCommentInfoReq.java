/*
 * Copyright (c) 2025. Bubble
 */

package com.bubble.pilipili.interact.pojo.req;

import com.bubble.pilipili.common.http.PageQueryReq;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @author Bubble
 * @date 2025.03.05 16:47
 */
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PageQueryCommentInfoReq extends PageQueryReq {

//    /**
//     * 回复评论ID
//     */
//    private Integer parentId;
    /**
     * 回复评论根ID（这里用于查某条评论的所有回复）
     */
    private Integer parentRootId;

    /**
     * 回复对象类型及ID（用于查询某对象的所有评论）
     */
    private Integer relaType;
    private Integer relaId;
}
