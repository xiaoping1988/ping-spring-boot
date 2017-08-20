package com.mybatis.ping.spring.boot.enums;

/**
 * 条件参数值的数据库类型
 * 创建者： 刘江平
 * 创建时间：2015年7月7日下午6:33:56
 */
public enum DbType {
	/**字符串类型*/
	STRING,
	/**日期类型*/
	DATE,
	/**整数类型*/
	INT,
	/**浮点数类型*/
	FLOAT;

	public static String getNames(){
		StringBuilder sb = new StringBuilder("[");
		for (DbType dt:DbType.values()) {
			sb.append(",");
			sb.append(dt.toString());
		}
		sb.append("]");
		return sb.toString().replaceFirst(",","");
	}

	public static void main(String[] args) {
		System.out.println(getNames());
	}
}
