package com.mybatis.ping.spring.boot.enums;

/**
 * sql操作符
 * 创建者： 刘江平
 * 创建时间：2015年7月7日下午6:29:31
 */
public enum Operator {
	/**解析成SQL操作符为： >  (大于等于)*/
	GT(">"),
	/**解析成SQL操作符为： < (小于等于)*/
	LT("<"),
	/**解析成SQL操作符为： >=  (大于等于)*/
	GTEQ(">="),
	/**解析成SQL操作符为： <= (小于等于)*/
	LTEQ("<="),
	/**解析成SQL操作符为： like (模糊匹配)*/
	LIKE("like"),
	/**解析成SQL操作符为： not like (模糊匹配)*/
	NOTLIKE("not like"),
	/**解析成SQL操作符为： = (等于)*/
	EQ("="),
	/**解析成SQL操作符为： != (不等于)*/
	NQ("!="),
	/**解析成sql操作符为： in */
	IN("in"),
	/**解析成sql操作符为： not in */
	NOTIN("not in");
	
	private String operator;
	private Operator(String operator){
		this.operator = operator;
	}
	public String getOperator() {
		return operator;
	}

	public static String getNames(){
		StringBuilder sb = new StringBuilder("[");
		for (Operator op:Operator.values()) {
			sb.append(",");
			sb.append(op.toString());
		}
		sb.append("]");
		return sb.toString().replaceFirst(",","");
	}
	public static void main(String[] args) {
		System.out.println(getNames());
	}
}
