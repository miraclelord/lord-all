package com.lord.common.dto;

import java.io.Serializable;

/**
 * 功能：查询的基本参数
 *
 * @author xiaocheng
 * @version 1.0
 * @Date 2017年04月02日 14:56
 */
public class QueryParams implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 主键Id
     */
    private String id;
    /**
     * 名称，模糊查询
     */
    private String name;

    /** 每页的大小 */
    private Integer pageSize = 10;

    /** 当前页数 */
    private Integer page = 1;

    public String getId() {
        return id;
    }

    public Long getLongId() {
        if (id == null) {
            return null;
        }
        return Long.parseLong(id);
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }
}
