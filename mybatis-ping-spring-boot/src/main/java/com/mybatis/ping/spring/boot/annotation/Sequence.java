package com.mybatis.ping.spring.boot.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * oracle数据库的自增序列，只用实体的唯一主键属性上
 * 创建者： 刘江平
 * 创建时间：2015年7月6日下午4:46:57
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface Sequence {
	/**
	 * 序列名称
	 * @return
	 */
	public abstract String value();
}
