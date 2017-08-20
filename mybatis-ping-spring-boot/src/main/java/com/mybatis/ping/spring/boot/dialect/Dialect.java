package com.mybatis.ping.spring.boot.dialect;

import com.mybatis.ping.spring.boot.meta.PropertyInfo;

import java.util.List;


/**
 * 数据库方言接口
 * <br>创建者： 刘江平
 * 创建时间：2015年8月7日下午6:40:07
 */
public interface Dialect {
	/**
	 * 获取分页sql
	 * @param querySql 查询全结果集的sql
	 * @param startindex 起始行索引  结果集不含此行  即>
	 * @param rows 分页行数
	 * @return
	 */
	String getPaginateSql(String querySql, int startindex, int rows);
	/**
	 * 获取插入SQL
	 * @param tableName 表名称
	 * @param propertyInfos 表所有列
	 * @return
	 */
	String getInsertSql(String tableName, List<PropertyInfo> propertyInfos);
}
