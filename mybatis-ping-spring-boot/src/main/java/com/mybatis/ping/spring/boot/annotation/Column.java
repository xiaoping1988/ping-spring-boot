package com.mybatis.ping.spring.boot.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * java实体bean的属性对应的数据库表字段名称
 * 创建者： 刘江平
 * 创建时间：2015年7月8日下午2:51:29
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface Column {
	/**
	 * 数据字段名称
	 * @return
	 */
	public abstract String value();
}
