package com.mybatis.ping.spring.boot.vo;

import com.mybatis.ping.spring.boot.enums.Operator;

/**
 * sql查询条件，字段的逻辑比较符
 * <br>创建者： 刘江平
 * 创建时间：2016年1月28日下午3:31:13
 */
public class DbOperator {
	/**数据库字段名称*/
	private String dbColumnName;
	/**sql查询时，此字段的逻辑比较符*/
	private Operator operator=Operator.LIKE;
	public String getDbColumnName() {
		return dbColumnName;
	}
	public void setDbColumnName(String dbColumnName) {
		this.dbColumnName = dbColumnName;
	}
	public Operator getOperator() {
		return operator;
	}
	public void setOperator(Operator operator) {
		this.operator = operator;
	}
	
}
