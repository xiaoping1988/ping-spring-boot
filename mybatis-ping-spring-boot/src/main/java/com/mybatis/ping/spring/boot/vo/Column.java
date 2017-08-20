package com.mybatis.ping.spring.boot.vo;


import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;


/**
 * 字段
 * <br>创建者： 刘江平
 * 创建时间：2016年5月11日下午1:34:06
 */
public class Column implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 6409963462567391013L;
	/**
	 * 字段名
	 */
	private String column;
	/**
	 * 字段值
	 */
	private Object value;
	/**
	 * 
	 * @param column 字段名
	 * @param value 字段值
	 */
	public Column(String column, Object value) {
		if(StringUtils.isBlank(column)){
			throw new RuntimeException("column不能为空!");
		}
		this.column = column;
		this.value = value;
	}
	public String getColumn() {
		return column;
	}
	public void setColumn(String column) {
		this.column = column;
	}
	public Object getValue() {
		return value;
	}
	public void setValue(Object value) {
		this.value = value;
	}

}
