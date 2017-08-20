package com.mybatis.ping.spring.boot.extend.service;

import com.mybatis.ping.spring.boot.vo.Pagination;
import org.mybatis.spring.SqlSessionTemplate;

import java.util.List;
import java.util.Map;

/**
 * 根据mapper文件中id进行的通用查询服务
 * @author 刘江平 2017年1月14日下午6:14:18
 *
 */
public class MybatisQueryService {
	private SqlSessionTemplate sqlSessionTemplate;
	
	public MybatisQueryService(SqlSessionTemplate sqlSessionTemplate) {
		this.sqlSessionTemplate = sqlSessionTemplate;
	}
	/**
	 * 查询一条记录
	 * @param mapperSelectId mapper文件中select的id
	 * @return
	 */
	public <T> T selectOne(String mapperSelectId){
		return this.sqlSessionTemplate.selectOne(mapperSelectId);
	}
	/**
	 * 查询一条记录
	 * @param mapperSelectId mapper文件中select的id
	 * @param paramters 条件参数
	 * @return
	 */
	public <T> T selectOne(String mapperSelectId,Map<String,Object> paramters){
		return this.sqlSessionTemplate.selectOne(mapperSelectId,paramters);
	}
	/**
	 * 查询多条记录
	 * @param mapperSelectId mapper文件中select的id
	 * @return
	 */
	public <T> List<T> selectList(String mapperSelectId){
		return this.sqlSessionTemplate.selectList(mapperSelectId);
	}
	/**
	 * 查询多条记录
	 * @param mapperSelectId mapper文件中select的id
	 * @param paramters 条件参数
	 * @return
	 */
	public <T> List<T> selectList(String mapperSelectId,Map<String,Object> paramters){
		return this.sqlSessionTemplate.selectList(mapperSelectId,paramters);
	}
	/**
	 * 分页查询多条记录
	 * @param mapperSelectId mapper文件中select的id
	 * @param pagination 分页对象
	 * @return
	 */
	public <T> List<T> selectList(String mapperSelectId,Pagination pagination){
		return this.sqlSessionTemplate.selectList(mapperSelectId,pagination);
	}
	/**
	 * 分页查询多条记录
	 * @param mapperSelectId mapper文件中select的id
	 * @param paramters 条件参数
	 * @param pagination 分页对象
	 * @return
	 */
	public <T> List<T> selectList(String mapperSelectId,Map<String,Object> paramters,Pagination pagination){
		paramters.put("pagination", pagination);
		return this.sqlSessionTemplate.selectList(mapperSelectId,paramters);
	}
}
