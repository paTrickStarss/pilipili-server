/*
 * Copyright (c) 2025. Bubble
 */

package com.bubble.pilipili.interact.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bubble.pilipili.interact.pojo.entity.DynamicAttach;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author Bubble
 * @date 2025.03.01 15:28
 */
@Mapper
public interface DynamicAttachMapper extends BaseMapper<DynamicAttach> {


//    @Select("select * from dynamic_attach where did = ${did})")
//    List<DynamicAttach> selectDynamicAttachByDidList(List<Integer> didList);
}
