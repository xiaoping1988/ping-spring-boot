package com.mybatis.ping.spring.boot.dialect;

import com.mybatis.ping.spring.boot.enums.DbDialect;

/**
 * 数据库方言工厂类
 * <br>创建者： 刘江平
 * 创建时间：2015年8月10日下午3:21:29
 */
public class DialectFactory {
	/**
	 * 获取数据库方言类
	 * @param dbDialect 方言类性
	 * @return
	 */
	public static Dialect getDialect(String dbDialect){
		if(DbDialect.MYSQL.toString().equalsIgnoreCase(dbDialect)){
			return new MysqlDialect();
		}else if(DbDialect.ORACLE.toString().equalsIgnoreCase(dbDialect)){
			return new OracleDialect();
		}
		return null;
	}
}
