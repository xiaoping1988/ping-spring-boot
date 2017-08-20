package com.mybatis.ping.spring.boot.meta;

import com.mybatis.ping.spring.boot.enums.DbType;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * 实体的一个属性元信息
 * 创建者： 刘江平
 * 创建时间：2015年7月6日下午2:18:49
 */
public class PropertyInfo {
	/**属性字段*/
	private Field field;
	/**实体属性名称*/
	private String propertyName;
	/**实体属性名称对应的数据库字段名称*/
	private String dbColumnName;
	/**是否为主键,true是主键，false则不是*/
	private boolean pk = false;
	/**是否要根据此字段排序，值为(DESC,ASC)则会排序，其他则不排序*/
	private String orderBy;
	/**oracle数据库的自增序列名称，只能用于唯一主键上*/
	private String sequence;
	/**属性的类型*/
	private Class<?> returnType;
	/**get方法*/
	private Method readMethod;
	/**是否自增序列*/
	private boolean increment = false;

	/**
	 * 根据该字段的returnType获取对应的数据库类型
	 * @return
	 */
	public DbType getDbType(){
		String simpleName = this.returnType.getSimpleName();
		DbType dtType = DbType.STRING;
		if(simpleName.equals("String")){
			dtType = DbType.STRING;
		}else if(simpleName.equals("int")
			   ||simpleName.equals("Integer")
			   ||simpleName.equals("long")
			   ||simpleName.equals("Long")){
			dtType = DbType.INT;
		}else if(simpleName.equals("float")
			   ||simpleName.equals("Float")
			   ||simpleName.equals("double")
			   ||simpleName.equals("Double")
			   ||simpleName.equals("BigDecimal")){
			dtType = DbType.FLOAT;
		}else if(simpleName.equals("Date")
			   ||simpleName.equals("Timestamp")){
			dtType = DbType.DATE;
		}
		return dtType;
	}

	public Field getField() {
		return field;
	}

	public void setField(Field field) {
		this.field = field;
	}

	public String getPropertyName() {
		return propertyName;
	}

	public void setPropertyName(String propertyName) {
		this.propertyName = propertyName;
	}

	public String getDbColumnName() {
		return dbColumnName;
	}

	public void setDbColumnName(String dbColumnName) {
		this.dbColumnName = dbColumnName;
	}

	public boolean isPk() {
		return pk;
	}

	public void setPk(boolean pk) {
		this.pk = pk;
	}

	public String getOrderBy() {
		return orderBy;
	}

	public void setOrderBy(String orderBy) {
		this.orderBy = orderBy;
	}

	public String getSequence() {
		return sequence;
	}

	public void setSequence(String sequence) {
		this.sequence = sequence;
	}

	public Class<?> getReturnType() {
		return returnType;
	}

	public void setReturnType(Class<?> returnType) {
		this.returnType = returnType;
	}

	public Method getReadMethod() {
		return readMethod;
	}

	public void setReadMethod(Method readMethod) {
		this.readMethod = readMethod;
	}

	public boolean isIncrement() {
		return increment;
	}

	public void setIncrement(boolean increment) {
		this.increment = increment;
	}
}
