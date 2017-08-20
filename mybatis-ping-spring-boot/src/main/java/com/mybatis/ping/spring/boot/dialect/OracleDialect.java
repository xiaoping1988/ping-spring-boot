package com.mybatis.ping.spring.boot.dialect;

import com.mybatis.ping.spring.boot.meta.PropertyInfo;
import org.apache.commons.lang3.StringUtils;

import java.util.List;


/**
 * Oracle数据库方言
 * <br>创建者： 刘江平
 * 创建时间：2015年8月10日下午3:07:26
 */
public class OracleDialect implements Dialect {

	public String getPaginateSql(String querySql, int startindex, int rows) {
		if (StringUtils.isBlank(querySql)) {
			return querySql;
		}
		querySql = querySql.replaceAll("[\r\n]", " ").replaceAll("\\s{2,}", " ");
		StringBuffer pagingSelect = new StringBuffer(querySql.length() + 100);
		pagingSelect.append("select * from ( select row_.*, rownum rownum_ from ( ");
		pagingSelect.append(querySql);
		pagingSelect.append(" ) row_ ) where rownum_ > " + startindex
				+ " and rownum_ <= " + (startindex + rows));
		return pagingSelect.toString();
	}

	public String getInsertSql(String tableName, List<PropertyInfo> propertyInfos) {
		StringBuilder insertSql = new StringBuilder();
		StringBuilder valueSql = new StringBuilder();
		int size = propertyInfos.size();
		/**判断是否是唯一主键,如果值为1代表是，否则不是*/
		int isPrimaryKey=0;
		PropertyInfo primaryKey = null;
		for (PropertyInfo propertyInfo : propertyInfos) {
			boolean pk = propertyInfo.isPk();
			if(pk){
				isPrimaryKey++;
				primaryKey = propertyInfo;
			}
		}
		
		if(isPrimaryKey==1){//此表是唯一主键
			String propertyName = primaryKey.getPropertyName();
			String sequence = primaryKey.getSequence();
			if(!StringUtils.isBlank(sequence)){//是否自增序列
				String resultType="int";
				if(primaryKey.getReturnType().isAssignableFrom(Long.class)||primaryKey.getReturnType().isAssignableFrom(long.class)){
					resultType="long";
				}
				insertSql.append("<selectKey keyProperty=\""+propertyName+"\" resultType=\""+resultType+"\" order=\"BEFORE\">")
				.append("SELECT "+sequence.trim()+".NEXTVAL FROM DUAL")
				.append("</selectKey> ");
			}
		}
		insertSql.append("INSERT INTO ").append(tableName).append("(");
		insertSql.append(" <trim suffix='' suffixOverrides=','>");
		valueSql.append(" <trim suffix='' suffixOverrides=','>");
		for (int i=0 ; i<size; i++) {
			PropertyInfo propertyInfo = propertyInfos.get(i);
			String propertyName = propertyInfo.getPropertyName();
			String dbColumnName = propertyInfo.getDbColumnName();
			insertSql.append("<if test=\""+propertyName+" != null\" >");
			insertSql.append(dbColumnName).append(",");
			insertSql.append("</if>");
			valueSql.append("<if test=\""+propertyName+" != null\" >");
			valueSql.append("#{").append(propertyName).append("}").append( ",") ;
			valueSql.append("</if>");
		}
		valueSql.append("</trim>");
		insertSql.append("</trim>) VALUES (").append(valueSql).append(")");
		return insertSql.toString();
	}

}
