package com.mybatis.ping.spring.boot;


import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * mybatis相关属性
 * Created by 刘江平 on 2016-10-13.
 */
@ConfigurationProperties(prefix = MybatisProperties.PREFIX)
public class MybatisProperties {
	public static final String PREFIX = "mybatis";

	/**
	 * mybatis的configLocation地址
	 */
	private String configLocation;
	/**
	 * mapper文件地址
	 */
	private String[] mapperLocations;
	/**
	 * mybatis相关mapper类和实体类的base包
	 */
	private String basePackage;


	public String getConfigLocation() {
		return configLocation;
	}

	public void setConfigLocation(String configLocation) {
		this.configLocation = configLocation;
	}

	public String[] getMapperLocations() {
		return mapperLocations;
	}

	public void setMapperLocations(String[] mapperLocations) {
		this.mapperLocations = mapperLocations;
	}

	public String getBasePackage() {
		return basePackage;
	}

	public void setBasePackage(String basePackage) {
		this.basePackage = basePackage;
	}
}
