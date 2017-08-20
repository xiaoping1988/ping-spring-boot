package com.mybatis.ping.spring.boot.vo;

import com.mybatis.ping.spring.boot.enums.DbType;
import com.mybatis.ping.spring.boot.enums.Operator;
import com.mybatis.ping.spring.boot.meta.BaseMeta;
import com.mybatis.ping.spring.boot.meta.BeanInfo;
import com.mybatis.ping.spring.boot.meta.PropertyInfo;
import com.mybatis.ping.spring.boot.utils.DateUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

/**
 * 通用查询条件的封装。目前支持AND条件
 * 创建者： 刘江平
 * 创建时间：2015年7月8日下午2:36:26
 */
public class Condition implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 6323255822270908073L;
	private static Logger log = LoggerFactory.getLogger(Condition.class);
	private static final String DEFAULT_DATE_PATTERN = "yyyy-MM-dd";
	private static final String DEFAULT_DATETIME_PATTERN = "yyyy-MM-dd HH:mm:ss";
	/**分隔符*/
	public static final String CONDITION_SEPARATOR = "\\|";
	/**查询条件名称的正则表达式*/
	public static final String CONDITION_PATTERN = "^\\w+"+CONDITION_SEPARATOR+"[a-zA-Z]+"+CONDITION_SEPARATOR+"[a-zA-Z]+$";//userid|int|eq
	/**数据库字段*/
	private String column;
	/**数据库字段类型*/
	private String dbType;
	/**sql操作符*/
	private String operator;
	/**条件参数值,一开始传入的原始类型值*/
	private Object value;
	/**条件参数值,转换后符合数据库类型的值*/
	private Object newValue;
	public Condition(){}
	/**
	 * sql操作符为in或者not in时，value可传数组、Collection集合、字符串(逗号分隔)，传其他类型则无效
	 * @param column 必须是数据库中字段名称
	 * @param dbType 数据库字段类型
	 * @param operator sql操作符
	 * @param value 条件参数值
	 */
	public Condition(String column, DbType dbType, Operator operator, Object value){
		this.column=column;
		this.dbType=dbType.toString();
		this.operator=" "+operator.getOperator()+" ";
		this.value=value;
		this.newValue=this.convertValue(dbType,operator,value);
	}
	/**
	 * sql操作符为in或者not in时，value可传数组、Collection集合、字符串(逗号分隔)，传其他类型则无效
	 * @param column 数据库字段或者java实体bean的属性字段
	 * @param dbType 数据库字段类型
	 * @param operator sql操作符
	 * @param value 条件参数值
	 * @param entityClass java实体bean
	 */
	public Condition(String column,DbType dbType,Operator operator,Object value,Class<?> entityClass){
		this(column, dbType, operator, value);
		BeanInfo beanInfo = BaseMeta.getBeanInfo(entityClass);
		if(beanInfo!=null){
			List<PropertyInfo> properties = beanInfo.getPropertyInfos();
			for (PropertyInfo pi : properties) {
				String dbColumnName = pi.getDbColumnName();
				String propertyName = pi.getPropertyName();
				if(column.equalsIgnoreCase(dbColumnName)||column.equalsIgnoreCase(propertyName)){
					this.column=dbColumnName;
					break;
				}
			}
		}
	}
	/**
	 * 转换传入的参数值符合数据库的类型
	 * @param dbType 数据库类型
	 * @param value 参数值
	 * @return
	 */
	private Object convertValue(DbType dbType,Operator operator,Object value) {
		if(value==null){
			return null;
		}
		//空串
		if(value instanceof String){
			if(StringUtils.isBlank(value.toString())){
				return null;
			}
		}
		Object tempValue = null;
		switch (operator) {
		case LIKE:
		case NOTLIKE:
			tempValue = "%"+((value instanceof Date)? DateUtils.formatDate((Date)value, DEFAULT_DATE_PATTERN):value.toString())+"%";
			break;
		case IN:
		case NOTIN:
			tempValue = (value instanceof String)?value.toString().split(","):value;
			break;
		default:
			switch (dbType) {
			case STRING:
				tempValue = (value instanceof Date)?DateUtils.formatDate((Date)value, DEFAULT_DATE_PATTERN):value.toString();
				break;
			case INT:
				tempValue = (value instanceof String)?Long.parseLong(value.toString()):value;
				break;
			case FLOAT:
				tempValue = (value instanceof String)?Float.parseFloat(value.toString()):value;
				break;
			case DATE:
				tempValue = (value instanceof Date)?DateUtils.formatDate((Date)value, DEFAULT_DATETIME_PATTERN):value;
				break;
			default:
				break;
			}
			break;
		}
		return tempValue;
	}
	/**
	 * 获取条件对象
	 * @param column_dbtype_operator 字段名称(数据库字段或者java实体bean的属性字段)、数据库类型和sql操作符组成的字符串，竖线"|"分隔，比如userid|int|eq <br>dbtype(数据库类型)的值域参照枚举DbType,operator(SQL操作符)的值域参照枚举Operator
	 * @param value 参数值
	 * @see DbType
	 * @see Operator
	 * @return
	 */
	public static Condition getCondition(String column_dbtype_operator,Object value,Class<?> entityClass){
		if(StringUtils.isBlank(column_dbtype_operator)){
			return new Condition();
		}
		column_dbtype_operator = column_dbtype_operator.trim();
		if(!Pattern.matches(CONDITION_PATTERN, column_dbtype_operator)){
			log.warn("传入的参数值["+column_dbtype_operator+"]格式不正确!正确格式为:字段名称(只能数字、字母或者下划线)、数据库类型"+ ArrayUtils.toString(DbType.values())+"和sql操作符"+ArrayUtils.toString((Operator.values()))+"，以'"+CONDITION_SEPARATOR+"'分隔!");
			return new Condition();
		}
		String[] strs = column_dbtype_operator.split(CONDITION_SEPARATOR);
		String column = strs[0];
		/**校验传入的数据库类型值是否符合规范*/
		DbType type = null;
		String dbtype = strs[1];
		DbType[] dbTypes = DbType.values();
		boolean isDbType = false;
		String dbtypes_str = "";
		for (DbType dbType2 : dbTypes) {
			String string = dbType2.toString();
			dbtypes_str = dbtypes_str+string+ ",";
			if(string.equalsIgnoreCase(dbtype)){
				type = dbType2;
				isDbType = true;
			}
		}
		dbtypes_str = dbtypes_str.substring(0, dbtypes_str.length()-1);
		if(!isDbType){
			log.warn("传入的数据库类型值["+dbtype+"]不符合规范!值域范围:["+dbtypes_str+"]");
			return new Condition();
		}
		/**校验传入的sql操作符是否符合规范*/
		Operator op = null;
		String operator = strs[2];
		Operator[] operators = Operator.values();
		boolean isOperator = false;
		String operator_str = "";
		for (Operator operator2 : operators) {
			String string = operator2.toString();
			operator_str = operator_str+string+ ",";
			if(string.equalsIgnoreCase(operator)){
				op = operator2;
				isOperator = true;
			}
		}
		operator_str = operator_str.substring(0, operator_str.length()-1);
		if(!isOperator){
			log.warn("传入的sql操作符["+operator+"]不符合规范!值域范围:["+operator_str+"]");
			return new Condition();
		}
		return new Condition(column,type,op,value,entityClass);
	}
	/**
	 * 获取条件对象
	 * @param column_dbtype_operator 数据库字段名称、数据库类型和sql操作符组成的字符串，竖线"|"分隔，比如userid|int|eq <br>dbtype(数据库类型)的值域参照枚举DbType,operator(SQL操作符)的值域参照枚举Operator
	 * @param value 参数值
	 * @see DbType
	 * @see Operator
	 * @return
	 */
	public static Condition getCondition(String column_dbtype_operator,Object value){
		return getCondition(column_dbtype_operator, value, null);
	}
	public String getColumn() {
		return column;
	}
	public void setColumn(String column) {
		this.column = column;
	}
	public String getDbType() {
		return dbType;
	}
	public void setDbType(String dbType) {
		this.dbType = dbType;
	}
	public String getOperator() {
		return operator;
	}
	public void setOperator(String operator) {
		this.operator = operator;
	}
	public Object getValue() {
		return value;
	}
	public void setValue(Object value) {
		this.value = value;
	}
	public Object getNewValue() {
		return newValue;
	}
	public void setNewValue(Object newValue) {
		this.newValue = newValue;
	}
	@Override
	public String toString() {
		return "Condition [column=" + column + ", dbType=" + dbType
				+ ", operator=" + operator + ", value=" + value + "]";
	}
}
