package com.mybatis.ping.spring.boot.core;

import org.springframework.core.io.Resource;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * mapper文件资源,把映射文件按正则表达式进行解析，为动态生成的和自己配的映射文件整合成一个文件做准备
 * Created by 刘江平 on 2016-10-13.
 */
public class MapperResource {
    /**
     * mapper内容的正则表达式
     */
    private static final Pattern pattern = Pattern.compile("<mapper *namespace *= *\"(.+?)\" *>([\\S\\s]*)</mapper>");
    /**
     * mapper文件对应的文本内容
     */
    private String mapperText;
    /**
     * mapper文件对应的命名空间
     */
    private String namespace;
    /**
     * mapper文件中的sql文本
     */
    private String sqlNodes;
    /**
     * mapper文件对应的resource资源
     */
    private Resource resource;

    MapperResource(String mapperText, Resource resource){
        this(mapperText);
        this.resource = resource;
    }

    public MapperResource(String mapperText){
        this.mapperText = mapperText;
        Matcher matcher = pattern.matcher(mapperText);
        if(matcher.find()){
            this.namespace = matcher.group(1);
            this.sqlNodes = matcher.group(2);
        }else{
            throw new RuntimeException("mapper文件文本解析出错,未找到namespace或者sqlNnodes."+mapperText);
        }
    }

	public String getMapperText() {
		return mapperText;
	}

	public void setMapperText(String mapperText) {
		this.mapperText = mapperText;
	}

	public String getNamespace() {
		return namespace;
	}

	public void setNamespace(String namespace) {
		this.namespace = namespace;
	}

	public String getSqlNodes() {
		return sqlNodes;
	}

	public void setSqlNodes(String sqlNodes) {
		this.sqlNodes = sqlNodes;
	}

	public Resource getResource() {
		return resource;
	}

	public void setResource(Resource resource) {
		this.resource = resource;
	}
}
