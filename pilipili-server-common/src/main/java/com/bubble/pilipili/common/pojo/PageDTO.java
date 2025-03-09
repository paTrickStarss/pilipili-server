/*
 * Copyright (c) 2025. Bubble
 */

package com.bubble.pilipili.common.pojo;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

/**
 * 通用分页查询返回数据类
 * @author Bubble
 * @date 2025/01/22 23:08
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PageDTO<T> implements Serializable {

    private Long pageNo;
    private Long pageSize;
    private Long total;
    private List<T> data;

    public static <T> PageDTO<T> createPageDTO(Page<T> page) {
        PageDTO<T> pageDTO = new PageDTO<>();
        pageDTO.setPageNo(page.getCurrent());
        pageDTO.setPageSize(page.getSize());
        pageDTO.setTotal(page.getTotal());
        pageDTO.setData(page.getRecords());
        return pageDTO;
    }

    /**
     * 生成PageDTO
     * @param page 用于填充pageNo、pageSize、total
     * @param data 填充data
     * @return
     * @param <T> DTO类
     */
    public static <T> PageDTO<T> createPageDTO(Page<?> page, List<T> data) {
        PageDTO<T> pageDTO = new PageDTO<>();
        pageDTO.setPageNo(page.getCurrent());
        pageDTO.setPageSize(page.getSize());
        pageDTO.setTotal(page.getTotal());
        pageDTO.setData(data);
        return pageDTO;
    }
    public static <T> PageDTO<T> createPageDTO(Long pageNo, Long pageSize, Long total, List<T> data) {
        PageDTO<T> pageDTO = new PageDTO<>();
        pageDTO.setPageNo(pageNo);
        pageDTO.setPageSize(pageSize);
        pageDTO.setTotal(total);
        pageDTO.setData(data);
        return pageDTO;
    }
    public static <T> PageDTO<T> emptyPageDTO() {
        PageDTO<T> pageDTO = new PageDTO<>();
        pageDTO.setPageNo(0L);
        pageDTO.setPageSize(0L);
        pageDTO.setTotal(0L);
        pageDTO.setData(Collections.emptyList());
        return pageDTO;
    }

    public PageDTO<T> setData(List<T> data) {
        this.data = data;
        return this;
    }

}
