package com.mybatis.ping.spring.boot.core;

import com.mybatis.ping.spring.boot.meta.PropertyInfo;

import java.util.List;

/**
 * 生成mapper文件的crud操作的sql
 * Created by 刘江平 on 2016-10-13.
 */
public interface MapperCRUDBuilder {

    String findByPk(String tableName, List<PropertyInfo> propertyInfos);
    String find(String tableName, List<PropertyInfo> propertyInfos);
    String insert(String tableName, List<PropertyInfo> propertyInfos);
    String updateByPk(String tableName, List<PropertyInfo> propertyInfos);
    String updateBatchColumns(String tableName, List<PropertyInfo> propertyInfos);
    String deleteByPk(String tableName, List<PropertyInfo> propertyInfos);
    String deleteBatch(String tableName, List<PropertyInfo> propertyInfos);
    String count(String tableName, List<PropertyInfo> propertyInfos);
    String countDistinctColumns(String tableName, List<PropertyInfo> propertyInfos);
    String sumColumn(String tableName, List<PropertyInfo> propertyInfos);
    String maxColumn(String tableName, List<PropertyInfo> propertyInfos);
    String minColumn(String tableName, List<PropertyInfo> propertyInfos);
    String avgColumn(String tableName, List<PropertyInfo> propertyInfos);
}
