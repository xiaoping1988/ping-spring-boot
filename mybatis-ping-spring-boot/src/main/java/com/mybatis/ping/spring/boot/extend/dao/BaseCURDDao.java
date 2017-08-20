package com.mybatis.ping.spring.boot.extend.dao;

import com.mybatis.ping.spring.boot.extend.entity.BaseModel;
import com.mybatis.ping.spring.boot.vo.Column;
import com.mybatis.ping.spring.boot.vo.Condition;

import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 所有要支持curd功能的dao都必须继承此类，所有数据库实体对应的dao的名称格式必须实体名称+Dao,
 * 包路径是实体包路径+".dao"
 * Created by 刘江平 on 2016-10-14.
 */
public interface BaseCURDDao<T extends BaseModel> extends BaseQueryDao<T>{

    /**
     * 新增一条记录
     * @param entity
     */
    void insert(T entity);
    /**
     * 根据主键修改一条记录,为null的属性不修改
     * @param entity
     */
    void updateByPk(T entity);
    /**
     * 按条件批量修改某个字段的值
     * @param columns 要修改的字段信息
     * @param conditions 条件
     * @return
     */
    void updateBatchColumns(@Param(value = "columns") List<Column> columns, @Param(value = "conditions") List<Condition> conditions);
    /**
     * 根据主键删除唯一一条记录
     * @param entity
     */
    void deleteByPk(T entity);
    /**
     * 按条件批量删除
     * @param conditions
     */
    void deleteBatch(@Param(value = "conditions") List<Condition> conditions);
 
}
