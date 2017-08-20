package com.mybatis.ping.spring.boot.extend.service;

import com.mybatis.ping.spring.boot.enums.DbType;
import com.mybatis.ping.spring.boot.enums.Operator;
import com.mybatis.ping.spring.boot.extend.dao.BaseQueryDao;
import com.mybatis.ping.spring.boot.extend.entity.BaseModel;
import com.mybatis.ping.spring.boot.extend.entity.BaseTimeModel;
import com.mybatis.ping.spring.boot.meta.BaseMeta;
import com.mybatis.ping.spring.boot.meta.BeanInfo;
import com.mybatis.ping.spring.boot.meta.PropertyInfo;
import com.mybatis.ping.spring.boot.vo.Condition;
import com.mybatis.ping.spring.boot.vo.DbOperator;
import com.mybatis.ping.spring.boot.vo.Page;
import com.mybatis.ping.spring.boot.vo.Pagination;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * 支持查询的service基类
 * Created by 刘江平 on 2016-10-14.
 */
public abstract class BaseQueryService<T extends BaseModel, K extends BaseQueryDao<T>> extends BaseService{
    private static final Logger logger = LoggerFactory.getLogger(BaseQueryService.class);
    protected final List<Condition> emptyConditions = new ArrayList<Condition>();

    /**
     * 实体类型
     */
    protected Class<T> entityClass;
    /**
     * 实体是否继承了BaseTimeModel类,用来判断是否要注入属性create_time和update_time的值
     */
    protected boolean isBaseTimeModel;
    /**
     * dao原型属性
     */
    protected K baseDao;

    /**
     * 根据K泛型自动装载BaseDao
     *
     * @param baseDao
     */
    @SuppressWarnings("unchecked")
	@Autowired
    final void setBaseDao(K baseDao) {
        this.baseDao = baseDao;
        Type genType = getClass().getGenericSuperclass();
        Type[] params = ((ParameterizedType) genType).getActualTypeArguments();
        entityClass = (Class<T>) params[0];
        if (BaseTimeModel.class.isAssignableFrom(entityClass)) {
            this.isBaseTimeModel = true;
        }
    }

    /**
     * 根据主键查询唯一记录
     *
     * @param entity
     * @return
     */
    public T findByPk(T entity) {
        return baseDao.findByPk(entity);
    }

    /**
     * 根据分页和条件进行查询。如果不需要分页，把pagination设为null。 主要是为了方便一个条件的查询，不用在调用时自己封装成List
     *
     * @param pagination 传null则不分页
     * @param condition
     * @return
     */
    public Page<T> find(Pagination pagination, Condition condition) {
        List<Condition> conditions = null;
        if (condition != null) {
            conditions = new ArrayList<Condition>();
            conditions.add(condition);
        }
        List<T> rows = baseDao.find(pagination, conditions, null);
        return new Page<T>(rows, pagination);
    }

    /**
     * 根据分页和条件进行查询。多条件查询
     *
     * @param pagination 传null则不分页
     * @param conditions
     * @return
     */
    public Page<T> find(Pagination pagination, List<Condition> conditions) {
        List<T> rows = baseDao.find(pagination, conditions, null);
        return new Page<T>(rows, pagination);
    }

    /**
     * 根据条件进行查询主要是为了方便一个条件的查询，不用在调用时自己封装成List
     *
     * @param condition
     * @return
     */
    public List<T> find(Condition condition) {
        List<Condition> conditions = null;
        if (condition != null) {
            conditions = new ArrayList<Condition>();
            conditions.add(condition);
        }
        return baseDao.find(null, conditions, null);
    }
    
    /**
     * 根据多条件条件进行查询
     *
     * @param conditions
     * @param orderby    排序字符串，比如"order by id desc",传空，则按类字段注解上的OrderBy排序
     * @return
     */
    public List<T> find(List<Condition> conditions, String orderby) {
        return baseDao.find(null, conditions, orderby);
    }
    
    /**
     * 根据多条件进行查询
     *
     * @param conditions
     * @return
     */
    public List<T> find(List<Condition> conditions) {
        return baseDao.find(null, conditions, null);
    }

    /**
     * 查找所有的记录
     *
     * @return
     */
    public List<T> findAll() {
        return baseDao.find(null, emptyConditions, null);
    }

    /**
     * 对所有数据分页查询
     *
     * @param pagination 传null则不分页
     * @return
     */
    public Page<T> find(Pagination pagination) {
        List<T> rows = baseDao.find(pagination, emptyConditions, null);
        return new Page<T>(rows, pagination);
    }

    /**
     * 根据分页和条件进行查询。如果不需要分页，把pagination设为null。 主要是为了方便一个条件的查询，不用在调用时自己封装成List
     *
     * @param pagination 传null则不分页
     * @param condition
     * @param orderby    排序字符串，比如"order by id desc",传空，则按类字段注解上的OrderBy排序
     * @return
     */
    public Page<T> find(Pagination pagination, Condition condition, String orderby) {
        List<Condition> conditions = null;
        if (condition != null) {
            conditions = new ArrayList<Condition>();
            conditions.add(condition);
        }
        return new Page<T>(baseDao.find(pagination, conditions, orderby), pagination);
    }

    /**
     * 根据分页和条件进行查询。多条件查询
     *
     * @param pagination 传null则不分页
     * @param conditions
     * @param orderby    排序字符串，比如"order by id desc",传空，则按类字段注解上的OrderBy排序
     * @return
     */
    public Page<T> findByConditions(Pagination pagination, List<Condition> conditions, String orderby) {
        return new Page<T>(baseDao.find(pagination, conditions, orderby), pagination);
    }

    /**
     * 根据条件进行查询主要是为了方便一个条件的查询，不用在调用时自己封装成List
     *
     * @param condition
     * @param orderby   排序字符串，比如"order by id desc",传空，则按类字段注解上的OrderBy排序
     * @return
     */
    public List<T> find(Condition condition, String orderby) {
        List<Condition> conditions = null;
        if (condition != null) {
            conditions = new ArrayList<Condition>();
            conditions.add(condition);
        }
        return baseDao.find(null, conditions, orderby);
    }

    /**
     * 查找所有的记录
     *
     * @param orderby 排序字符串，比如"order by id desc",传空，则按类字段注解上的OrderBy排序
     * @return
     */
    public List<T> findAll(String orderby) {
        return baseDao.find(null, emptyConditions, orderby);
    }

    /**
     * 对所有数据分页查询
     *
     * @param pagination 传null则不分页
     * @param orderby    排序字符串，比如"order by id desc",传空，则按类字段注解上的OrderBy排序
     * @return
     */
    public Page<T> find(Pagination pagination, String orderby) {
        return new Page<T>(baseDao.find(pagination, emptyConditions, orderby), pagination);
    }
    
    /**
     * 数据库字段的sql操作符默认like或者eq, 整数类型是eq，其他是like
     * @param pagination 传null则不分页
     * @param entity
     * @param orderby    排序字符串，比如"order by id desc",传空，则按类字段注解上的OrderBy排序
     * @param dbOperators 操作符数组
     * @return
     */
    public Page<T> find(Pagination pagination,T entity, String orderby, DbOperator... dbOperators) {
        return new Page<T>(baseDao.find(pagination, this.parserEntity(entity, dbOperators), orderby), pagination);
    }

    /**
     * 
     * 数据库字段的sql操作符默认like或者eq, 整数类型是eq，其他是like
     * @param entity
     * @param orderby   排序字符串，比如"order by id desc",传空，则按类字段注解上的OrderBy排序
     * @param dbOperators 操作符数组
     * @return
     */
    public List<T> find(T entity, String orderby, DbOperator... dbOperators) {
        return baseDao.find(null, this.parserEntity(entity, dbOperators), orderby);
    }

    /**
     * 按多条件查询总记录数
     *
     * @param conditions
     * @return
     */
    public long count(List<Condition> conditions) {
        return this.baseDao.count(conditions);
    }

    /**
     * 按单个条件查询总记录
     *
     * @param condition
     * @return
     */
    public long count(Condition condition) {
        List<Condition> conditions = null;
        if (condition != null) {
            conditions = new ArrayList<Condition>();
            conditions.add(condition);
        }
        return this.count(conditions);
    }

    /**
     * 查询总记录
     *
     * @return
     */
    public long count() {
        List<Condition> conditions = null;
        return this.count(conditions);
    }

    /**
     * 按多条件查询某个字段的求和总计
     *
     * @param column      要求和的字段
     * @param conditions
     * @return
     */
    public BigDecimal sum(String column, List<Condition> conditions) {
        return this.baseDao.sumColumn(column, conditions);
    }

    /**
     * 按单个条件查询某个字段的求和总计
     *
     * @param column     要求和的字段
     * @param condition
     * @return
     */
    public BigDecimal sum(String column, Condition condition) {
        List<Condition> conditions = null;
        if (condition != null) {
            conditions = new ArrayList<Condition>();
            conditions.add(condition);
        }
        return this.sum(column,conditions);
    }

    /**
     * 按多条件查询某个字段的最大值
     *
     * @param column      要最大值的字段
     * @param conditions
     * @return
     */
    public BigDecimal max(String column, List<Condition> conditions) {
        return this.baseDao.maxColumn(column, conditions);
    }

    /**
     * 按单个条件查询某个字段的最大值
     *
     * @param column     要最大值的字段
     * @param condition
     * @return
     */
    public BigDecimal max(String column, Condition condition) {
        List<Condition> conditions = null;
        if (condition != null) {
            conditions = new ArrayList<Condition>();
            conditions.add(condition);
        }
        return this.max(column,conditions);
    }

    /**
     * 按多条件查询某个字段的最小值
     *
     * @param column      要最小值的字段
     * @param conditions
     * @return
     */
    public BigDecimal min(String column, List<Condition> conditions) {
        return this.baseDao.minColumn(column, conditions);
    }

    /**
     * 按单个条件查询某个字段的最小值
     *
     * @param column     要最小值的字段
     * @param condition
     * @return
     */
    public BigDecimal min(String column, Condition condition) {
        List<Condition> conditions = null;
        if (condition != null) {
            conditions = new ArrayList<Condition>();
            conditions.add(condition);
        }
        return this.min(column, conditions);
    }

    /**
     * 按多条件查询某个字段的最大值
     *
     * @param column      要最大值的字段
     * @param conditions
     * @return
     */
    public String maxString(String column, List<Condition> conditions) {
        return this.baseDao.maxString(column, conditions);
    }

    /**
     * 按单个条件查询某个字段的最大值
     *
     * @param column     要最大值的字段
     * @param condition
     * @return
     */
    public String maxString(String column, Condition condition) {
        List<Condition> conditions = null;
        if (condition != null) {
            conditions = new ArrayList<Condition>();
            conditions.add(condition);
        }
        return this.maxString(column,conditions);
    }

    /**
     * 按多条件查询某个字段的最小值
     *
     * @param column      要最小值的字段
     * @param conditions
     * @return
     */
    public String minString(String column, List<Condition> conditions) {
        return this.baseDao.minString(column, conditions);
    }

    /**
     * 按单个条件查询某个字段的最小值
     *
     * @param column     要最小值的字段
     * @param condition
     * @return
     */
    public String minString(String column, Condition condition) {
        List<Condition> conditions = null;
        if (condition != null) {
            conditions = new ArrayList<Condition>();
            conditions.add(condition);
        }
        return this.minString(column, conditions);
    }

    /**
     * 按多条件查询某个字段的平均值
     *
     * @param column      要平均值的字段
     * @param conditions
     * @return
     */
    public BigDecimal avg(String column, List<Condition> conditions) {
        return this.baseDao.avgColumn(column, conditions);
    }

    /**
     * 按单个条件查询某个字段的平均值
     *
     * @param column     要平均值的字段
     * @param condition
     * @return
     */
    public BigDecimal avg(String column, Condition condition) {
        List<Condition> conditions = null;
        if (condition != null) {
            conditions = new ArrayList<Condition>();
            conditions.add(condition);
        }
        return this.avg(column, conditions);
    }

    /**
     * 按多条件查询某些字段的去重总记录数
     *
     * @param columns      要去重的字段名称，多个以逗号隔开,比如"username,loginname"
     * @param conditions
     * @return
     */
    public long countDistinct(String columns, List<Condition> conditions) {
        return this.baseDao.countDistinctColumns(columns,conditions);
    }

    /**
     * 按单个条件查询某个字段的去重总记录数
     *
     * @param columns     要去重的字段名称，多个以逗号隔开,比如"username,loginname"
     * @param condition
     * @return
     */
    public long countDistinct(String columns, Condition condition) {
        List<Condition> conditions = null;
        if (condition != null) {
            conditions = new ArrayList<Condition>();
            conditions.add(condition);
        }
        return this.countDistinct(columns,conditions);
    }

    /**
     * 将实体类转化为条件集合,日期类型将会拆分成两个两条件gt和lt
     *
     * @param obj         条件对象
     * @param dbOperators 操作符数组
     * @return 数据库字段的sql操作符默认like或者eq, 整数类型是eq，其他是like
     */
    public List<Condition> parserEntity(T obj, DbOperator... dbOperators) {
        if (obj == null) {
            return emptyConditions;
        }
        Class<?> clazz = obj.getClass();
        BeanInfo bi = BaseMeta.getBeanInfo(clazz);
        if (bi == null) {
            logger.info(clazz.getName() + "不是数据库实体类!没有继承BaseModel");
            return emptyConditions;
        }
        List<Condition> conditions = new ArrayList<Condition>();
        try {
            List<PropertyInfo> properties = bi.getPropertyInfos();
            for (PropertyInfo pi : properties) {
                String dbColumnName = pi.getDbColumnName();
                DbType dbType = pi.getDbType();
                Method readMethod = pi.getReadMethod();
                Object val = readMethod.invoke(obj);
                if(val != null){
                    if(dbType.toString().equals(DbType.DATE.toString())){
                        conditions.add(new Condition(dbColumnName, dbType, Operator.GTEQ, val));
                        conditions.add(new Condition(dbColumnName, dbType, Operator.LTEQ, val));
                    }else{
                        conditions.add(new Condition(dbColumnName, dbType, getOperator(dbType, dbColumnName, dbOperators), val));
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return conditions;
    }

    /**
     * 获取sql操作符
     *
     * @param dbType       数据库字段类型
     * @param dbColumnName 数据库字段名称
     * @param dbOperators  操作符数组
     * @return 如果操作符数组中不含传入字段的操作符，则默认返回like或者eq,整数类型是eq，其他是like
     */
    private Operator getOperator(DbType dbType, String dbColumnName, DbOperator[] dbOperators) {
        Operator defaultOper = Operator.LIKE;
        if (dbType.toString().equals(DbType.INT)) {
            defaultOper = Operator.EQ;
        }
        for (DbOperator dbo : dbOperators) {
            if (dbColumnName.equalsIgnoreCase(dbo.getDbColumnName())) {
                Operator operator = dbo.getOperator();
                return (operator == null) ? defaultOper : operator;
            }
        }
        return defaultOper;
    }
}
