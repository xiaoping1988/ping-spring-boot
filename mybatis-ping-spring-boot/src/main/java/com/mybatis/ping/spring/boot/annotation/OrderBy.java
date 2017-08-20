package com.mybatis.ping.spring.boot.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 排序，值域只能是DESC和ASC，其他则不排序
 * 创建者： 刘江平
 * 创建时间：2015年7月6日下午4:47:39
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface OrderBy {
	/**
	 * 排序方式
	 * 创建者： 刘江平
	 * 创建时间：2015年7月6日下午4:57:10
	 */
	public static enum Order{
		/**倒序*/
		DESC,
		/**顺序*/
		ASC};
	/**
	 * 排序方式，默认为DESC倒序
	 * @return
	 */
	public abstract Order value() default Order.DESC;
}
