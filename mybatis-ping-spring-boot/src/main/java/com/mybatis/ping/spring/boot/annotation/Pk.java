package com.mybatis.ping.spring.boot.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 主键,表示此字段是一个主键字段
 * 一个实体中有多个字段上有此注解，表示此实体的主键是联合主键,
 * 如果只有一个字段有次注解，表示此实体的主键是唯一主键
 * 创建者： 刘江平
 * 创建时间：2015年7月6日下午4:47:28
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface Pk {

}
