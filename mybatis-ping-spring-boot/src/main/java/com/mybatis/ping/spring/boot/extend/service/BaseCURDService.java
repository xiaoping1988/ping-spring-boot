package com.mybatis.ping.spring.boot.extend.service;

import com.mybatis.ping.spring.boot.extend.dao.BaseCURDDao;
import com.mybatis.ping.spring.boot.extend.entity.BaseModel;
import com.mybatis.ping.spring.boot.extend.entity.BaseTimeModel;
import com.mybatis.ping.spring.boot.vo.Column;
import com.mybatis.ping.spring.boot.vo.Condition;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 支持crud功能的service基类
 * Created by 刘江平 on 2016-10-14.
 */
public abstract class BaseCURDService<T extends BaseModel, K extends BaseCURDDao<T>> extends BaseQueryService<T, K>{

    /**
     * 根据主键删除唯一记录
     *
     * @param entity
     */
    public void deleteByPk(T entity) {
        baseDao.deleteByPk(entity);
    }

    /**
     * 批量删除
     *
     * @param entitys
     */
    public void deleteBatchByPks(List<T> entitys) {
        for (T entity : entitys) {
            this.deleteByPk(entity);
        }
    }

    /**
     * 删除所有
     */
    public void deleteAll() {
        baseDao.deleteBatch(emptyConditions);
    }

    /**
     * 按单个条件批量删除
     *
     * @param condition
     */
    public void deleteBatch(Condition condition) {
        List<Condition> conditions = null;
        if (condition != null) {
            conditions = new ArrayList<Condition>();
            conditions.add(condition);
        }
        baseDao.deleteBatch(conditions);
    }

    /**
     * 按多个条件批量删除
     *
     * @param conditions
     */
    public void deleteBatch(List<Condition> conditions) {
        baseDao.deleteBatch(conditions);
    }

    
    /**
     * 新增一条记录,
     * 如果实体继承了类BaseTimeModel,则会默认注入create_time和update_time的值为当前时间
     *
     * @param entity
     */
    public void save(T entity) {
        if (this.isBaseTimeModel) {
            BaseTimeModel e = (BaseTimeModel) entity;
            e.setCreate_time(new Date());
            e.setUpdate_time(new Date());
        }
        baseDao.insert(entity);
    }

    /**
     * 批量保存所有实体
     *
     * @param entities
     */
    public void saveBatch(List<T> entities) {
        if (!CollectionUtils.isEmpty(entities)) {
            for (T entity : entities) {
                save(entity);
            }
        }
    }

    /**
     * 根据主键修改一条记录,
     * 如果实体继承了类BaseTimeModel,则会默认注入update_time的值为当前时间
     *
     * @param entity
     */
    public void update(T entity) {
        if (this.isBaseTimeModel) {
            BaseTimeModel e = (BaseTimeModel) entity;
            e.setUpdate_time(new Date());
        }
        baseDao.updateByPk(entity);
    }

    /**
     * 按多个条件批量修改多个字段的值,
     *如果实体继承了类BaseTimeModel,则会默认注入update_time的值为当前时间
     * @param columns    要修改的字段
     * @param conditions 条件
     * @return
     */
    public void updateBatchColumns(List<Column> columns, List<Condition> conditions) {
        if(this.isBaseTimeModel){
            boolean noUpdateTime = true;
            for (Column c : columns) {
                if(c.getColumn().equalsIgnoreCase(BaseTimeModel.UPDATE_TIME_COLUMNNAME)){
                    noUpdateTime = false;
                }
            }
            if(noUpdateTime){
                columns.add(new Column(BaseTimeModel.UPDATE_TIME_COLUMNNAME,new Date()));
            }
        }
        this.baseDao.updateBatchColumns(columns, conditions);
    }

    /**
     * 按单个条件批量修改多个字段的值
     *
     * @param columns   要修改的字段
     * @param condition 条件
     * @return
     */
    public void updateBatchColumns(List<Column> columns, Condition condition) {
        List<Condition> conditions = null;
        if (condition != null) {
            conditions = new ArrayList<Condition>();
            conditions.add(condition);
        }
        this.updateBatchColumns(columns, conditions);
    }

    /**
     * 按多个条件批量修改某个字段的值
     *
     * @param column     字段名称
     * @param value      要修改的值
     * @param conditions 条件
     * @return
     */
    public void updateBatchColumn(String column, Object value, List<Condition> conditions) {
        List<Column> columns = new ArrayList<>();
        columns.add(new Column(column, value));
        this.updateBatchColumns(columns, conditions);

    }

    /**
     * 按单个条件批量修改某个字段的值
     *
     * @param column    字段名称
     * @param value     要修改的值
     * @param condition 条件
     * @return
     */
    public void updateBatchColumn(String column, Object value, Condition condition) {
        List<Condition> conditions = null;
        if (condition != null) {
            conditions = new ArrayList<Condition>();
            conditions.add(condition);
        }
        List<Column> columns = new ArrayList<>();
        columns.add(new Column(column, value));
        this.updateBatchColumns(columns, conditions);
    }

    /**
     * 更新list中所有的实体。
     *
     * @param entities
     */
    public void updateBatch(List<T> entities) {
        if (!CollectionUtils.isEmpty(entities)) {
            for (T entity : entities) {
                update(entity);
            }
        }
    }

}
