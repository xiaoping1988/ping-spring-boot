package com.mybatis.ping.spring.boot.vo;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

/**
 * Created by 刘江平 on 2016-10-14.
 */
@ApiModel("分页对象")
public class Page<T> {
    /**
     * 查询的结果集
      */
    @ApiModelProperty("查询结果集")
    private List<T> rows;
    @ApiModelProperty("总记录数")
    private long totalCount;
    @ApiModelProperty("总页数")
    private long totalPages;
    @ApiModelProperty("当前页数")
    private long currentPage;
    @ApiModelProperty("每页大小")
    private long pageSize;

    

	public Page(List<T> rows, Pagination pagination) {
		this.rows = rows;
		this.totalCount = (pagination!=null)?pagination.getTotalCount():((rows!=null)?rows.size():0);
        this.totalPages = (pagination!=null)?pagination.getTotalPages():1;
        this.currentPage = (pagination!=null)?pagination.getCurrentPage():1;
        this.pageSize = (pagination!=null)?pagination.getPageSize():((rows!=null)?rows.size():0);
	}

	/**
     * 获取查询结果集的总记录数
     * @return
     */
    public long getTotalCount(){
        return this.totalCount;
    }

    /**
     * 获取总页数
     * @return
     */
    public long getTotalPages(){
        return this.totalPages;
    }

    /**
     * 获取查询的当前页数
     * @return
     */
    public long getCurrentPage(){
        return this.currentPage;
    }

    /**
     * 获取每页查询的size
     * @return
     */
    public long getPageSize(){
        return this.pageSize;
    }

	public List<T> getRows() {
		return rows;
	}

	public void setRows(List<T> rows) {
		this.rows = rows;
	}
}
