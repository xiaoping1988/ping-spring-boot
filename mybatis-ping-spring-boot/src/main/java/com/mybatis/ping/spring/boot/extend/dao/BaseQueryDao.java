package com.mybatis.ping.spring.boot.extend.dao;

import com.mybatis.ping.spring.boot.extend.entity.BaseModel;
import com.mybatis.ping.spring.boot.vo.Condition;
import com.mybatis.ping.spring.boot.vo.Pagination;

import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;

/**
 * 所有只支持query的dao可继承此类，所有数据库实体对应的dao的名称格式必须实体名称+Dao,
 * 包路径是实体包路径+".dao"
 * Created by 刘江平 on 2016-10-14.
 */
public interface BaseQueryDao<T extends BaseModel> extends SqlMapper{

    /**
     * 根据主键查询唯一记录
     * @param entity
     * @return
     */
    T findByPk(T entity);

    /**
     * 按条件查询，并且分页
     * @param page 传null则不分页
     * @param conditions 查询条件,传空，则查询所有
     * @param orderby 排序字符串，比如"order by id desc",传空，则按类字段注解上的OrderBy排序
     * @return
     */
    List<T> find(Pagination page,
                 @Param(value = "conditions") List<Condition> conditions, @Param(value = "orderby") String orderby);

    /**
     * 按条件查询总记录
     * @param conditions
     * @return 无记录则返回0
     */
    long count(@Param(value = "conditions") List<Condition> conditions);
    /**
     * 按条件查询某些字段的去重总记录数
     * @param columns 要去重的字段名称，多个以逗号隔开
     * @param conditions
     * @return 无记录则返回0
     */
    long countDistinctColumns(@Param(value = "columns") String columns, @Param(value = "conditions") List<Condition> conditions);
    /**
     * 按条件查询某个字段的求和总计
     * @param column 要求和的字段名称
     * @param conditions
     * @return
     */
    BigDecimal sumColumn(@Param(value = "column") String column, @Param(value = "conditions") List<Condition> conditions);
    /**
     * 按条件查询某个字段的最大值
     * @param column 要最大值的字段名称
     * @param conditions
     * @return
     */
    BigDecimal maxColumn(@Param(value = "column") String column, @Param(value = "conditions") List<Condition> conditions);
    /**
     * 按条件查询某个字段的最小值
     * @param column 要最大值的字段名称
     * @param conditions
     * @return
     */
    BigDecimal minColumn(@Param(value = "column") String column, @Param(value = "conditions") List<Condition> conditions);
    /**
     * 按条件查询某个字段的平均值
     * @param column 要平均的字段名称
     * @param conditions
     * @return
     */
    BigDecimal avgColumn(@Param(value = "column") String column, @Param(value = "conditions") List<Condition> conditions);

    /**
     * 按条件查询某个字段的最大值
     * @param column 要最大值的字段名称
     * @param conditions
     * @return
     */
    String maxString(@Param(value = "column") String column, @Param(value = "conditions") List<Condition> conditions);
    /**
     * 按条件查询某个字段的最小值
     * @param column 要最大值的字段名称
     * @param conditions
     * @return
     */
    String minString(@Param(value = "column") String column, @Param(value = "conditions") List<Condition> conditions);

}
