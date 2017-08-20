package com.mybatis.ping.spring.boot.constant;

/**
 * Created by liujiangping on 2017/8/20.
 */
public class MybatisConstant {
    /**动态生成实体mapper文件的模板*/
    public static final String MYBATIS_ENTITY_MAPPER_TEMPLATE = "entity_mapper_template.xml";
    /**查询用的公用的mapper文件*/
    public static final String MYBATIS_COMMON_MAPPER = "common_mapper.xml";
    /**默认的mapperLocations*/
    public static final String DEFAULT_MAPPER_LOCATIONS = "classpath:mapper/*.xml,classpath:mapper/*/*.xml";
}
