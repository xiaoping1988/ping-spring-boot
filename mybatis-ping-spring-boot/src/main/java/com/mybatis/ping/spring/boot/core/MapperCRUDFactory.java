package com.mybatis.ping.spring.boot.core;

import com.mybatis.ping.spring.boot.annotation.OrderBy;
import com.mybatis.ping.spring.boot.constant.MybatisConstant;
import com.mybatis.ping.spring.boot.dialect.DialectFactory;
import com.mybatis.ping.spring.boot.meta.BaseMeta;
import com.mybatis.ping.spring.boot.meta.BeanInfo;
import com.mybatis.ping.spring.boot.meta.PropertyInfo;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 根据模板动态生成各个实体的mapper资源
 * Created by 刘江平 on 2016-10-13.
 */
public class MapperCRUDFactory implements MapperCRUDBuilder{

    private static final Logger logger = LoggerFactory.getLogger(MapperCRUDFactory.class);

    private static final String NEW_LINE_BREAK = "\r\n";
    /**条件参数*/
    private String dynamicConditions = " <include refid=\"common.dynamicConditions\"/> ";

    private static MapperCRUDFactory factory = null;

    private MapperCRUDFactory(){}

    public static synchronized MapperCRUDFactory getInstance(){
        if(factory==null){
            return new MapperCRUDFactory();
        }else{
            return factory;
        }
    }

    /**
     * 根据模板动态生成实体对应的mapper资源
     * @param beanInfo
     * @return
     */
    public MapperResource buildMapper(BeanInfo beanInfo) throws IOException {
        Map<String, String> context = new HashMap<>();
        String tableName = beanInfo.getTableName();
        List<PropertyInfo> propertyinfos = beanInfo.getPropertyInfos();
        String entityFullClassName = beanInfo.getEntityFullClassName();
        logger.info("生成实体["+entityFullClassName+"]对应的mapper文件资源!");
        context.put("findByPk", findByPk(tableName, propertyinfos));
        context.put("find", find(tableName, propertyinfos));
        context.put("insert", insert(tableName, propertyinfos));
        context.put("updateByPk", updateByPk(tableName, propertyinfos));
        context.put("updateBatchColumns", updateBatchColumns(tableName,propertyinfos));
        context.put("deleteByPk", deleteByPk(tableName,propertyinfos));
        context.put("deleteBatch", deleteBatch(tableName,propertyinfos));
        context.put("count", count(tableName,propertyinfos));
        context.put("countDistinctColumns", countDistinctColumns(tableName,propertyinfos));
        context.put("sum", sumColumn(tableName,propertyinfos));
        context.put("max", maxColumn(tableName,propertyinfos));
        context.put("min", minColumn(tableName,propertyinfos));
        context.put("avg", avgColumn(tableName,propertyinfos));
        //把entity替换成Dao
        context.put("daoClass", beanInfo.getEntityDaoFullClassName());
        context.put("entityClass", entityFullClassName);
        logger.info("匹配模板生成mapper文件的文本!");
        return new MapperResource(this.getMapperText(context));
    }


    private String getMapperText(Map<String, String> mapperContext) throws IOException {
        InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream(MybatisConstant.MYBATIS_ENTITY_MAPPER_TEMPLATE);
        String mapperString = IOUtils.toString(inputStream, "utf-8");
        for (Map.Entry<String, String> entry : mapperContext.entrySet()) {
            mapperString = mapperString.replaceAll("\\$\\{\\s*"+entry.getKey()+"\\s*\\}", entry.getValue().replaceAll("\\$", "\\\\\\$"));
        }
        return mapperString;
    }

    @Override
    public String findByPk(String tableName, List<PropertyInfo> propertyInfos) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT ");
        sql.append(this.dbColumnAsEntity(propertyInfos));
        sql.append(" FROM ");
        sql.append(tableName);
        sql.append(pkWhereSqlStr(propertyInfos));
        return sql.toString();
    }

    @Override
    public String find(String tableName, List<PropertyInfo> propertyInfos) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT ");
        sql.append(this.dbColumnAsEntity(propertyInfos));
        sql.append(" FROM ");
        sql.append(tableName);
        sql.append(NEW_LINE_BREAK).append(this.dynamicConditions);
        sql.append(NEW_LINE_BREAK);
        if (!CollectionUtils.isEmpty(propertyInfos)) {
            StringBuilder orderBySql = new StringBuilder();
            String orderby="";
            for (PropertyInfo propertyInfo : propertyInfos) {
                String propertyName = propertyInfo.getPropertyName();
                String orderBy = propertyInfo.getOrderBy();
                if(orderBy!=null&&(orderBy.equals(OrderBy.Order.ASC.toString())||orderBy.equals(OrderBy.Order.DESC.toString()))){
                    orderBySql.append(propertyName+" "+orderBy+",");
                }
            }
            if(!orderBySql.toString().equals("")){
                orderBySql.delete(orderBySql.length()-1, orderBySql.length());
                orderby = " order by "+orderBySql.toString();
            }
            sql.append(NEW_LINE_BREAK);
            sql.append("<choose>");
            sql.append("<when test=\"orderby!=null and orderby.trim()!=''\">${orderby}</when>");
            sql.append("<otherwise>");
            sql.append(orderby);
            sql.append("</otherwise>");
            sql.append("</choose>");
        }
        return sql.toString();
    }

    @Override
    public String insert(String tableName, List<PropertyInfo> propertyInfos) {
        return DialectFactory.getDialect(BaseMeta.db.toString()).getInsertSql(tableName,propertyInfos);
    }

    @Override
    public String updateByPk(String tableName, List<PropertyInfo> propertyInfos) {
        // <set>元素会动态前置 SET关键字,而且也会消除任意无关的逗号
        StringBuilder sql = new StringBuilder();
        sql.append("UPDATE ").append(tableName).append(" <set> ");
        for (PropertyInfo pi : propertyInfos) {
            String propertyName = pi.getPropertyName();
            String dbColumnName = pi.getDbColumnName();
            sql.append(" <if test=\"")
		            .append(propertyName)
		            .append("!= null\"> ")
		            .append(dbColumnName)
                    .append("=#{")
                    .append(propertyName)
                    .append("},")
                    .append(" </if> ");
        }
        sql.append(" </set> ");
        sql.append(pkWhereSqlStr(propertyInfos));
        return sql.toString();
    }

    @Override
    public String updateBatchColumns(String tableName, List<PropertyInfo> propertyInfos) {
        String sql = "UPDATE "+tableName+" SET <include refid=\"common.updateColumns\"/>"+this.dynamicConditions;
        return sql;
    }

    @Override
    public String deleteByPk(String tableName, List<PropertyInfo> propertyInfos) {
        StringBuilder sql = new StringBuilder();
        sql.append("DELETE FROM ").append(tableName);
        sql.append(pkWhereSqlStr(propertyInfos));
        return sql.toString();
    }

    @Override
    public String deleteBatch(String tableName, List<PropertyInfo> propertyInfos) {
        StringBuffer sql = new StringBuffer();
        sql.append("DELETE FROM ").append(tableName);
        sql.append(NEW_LINE_BREAK).append(dynamicConditions);
        return sql.toString();
    }

    @Override
    public String count(String tableName, List<PropertyInfo> propertyInfos) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT COUNT(*) FROM ").append(tableName);
        sql.append(this.dynamicConditions);
        return sql.toString();
    }

    @Override
    public String countDistinctColumns(String tableName, List<PropertyInfo> propertyInfos) {
        String sql = "SELECT COUNT(DISTINCT ${columns}) FROM " + tableName + this.dynamicConditions;
        return sql;
    }

    @Override
    public String sumColumn(String tableName, List<PropertyInfo> propertyInfos) {
        String sql = "SELECT SUM(${column}) FROM " + tableName + this.dynamicConditions;
        return sql;
    }

    @Override
    public String maxColumn(String tableName, List<PropertyInfo> propertyInfos) {
        String sql = "SELECT MAX(${column}) FROM " + tableName + this.dynamicConditions;
        return sql;
    }

    @Override
    public String minColumn(String tableName, List<PropertyInfo> propertyInfos) {
        String sql = "SELECT MIN(${column}) FROM " + tableName + this.dynamicConditions;
        return sql;
    }

    @Override
    public String avgColumn(String tableName, List<PropertyInfo> propertyInfos) {
        String sql = "SELECT AVG(${column}) FROM " + tableName + this.dynamicConditions;
        return sql;
    }

    /**
     * 将数据库字典名称全部取别名对应成java实体bean的属性名称
     * @param propertyInfos
     * @return
     */
    private String dbColumnAsEntity(List<PropertyInfo> propertyInfos){
        StringBuilder sql = new StringBuilder();
        int size = 0;
        if(!CollectionUtils.isEmpty(propertyInfos)){
            size = propertyInfos.size();
        }
        for (int i = 0; i < size; i++) {
            PropertyInfo propertyInfo = propertyInfos.get(i);
            if(i!=0){
                sql.append(",");
            }
            sql.append(propertyInfo.getDbColumnName()+" AS "+propertyInfo.getPropertyName());
        }
        return sql.toString();
    }

    /**
     * 主键where条件拼接
     * @param propertyInfos
     * @return
     */
    private String pkWhereSqlStr(List<PropertyInfo> propertyInfos) {
        if(CollectionUtils.isEmpty(propertyInfos)){
            return "";
        }
        int count=0;
        StringBuilder pkStr = new StringBuilder();
        for (PropertyInfo propertyInfo : propertyInfos) {
            boolean pk = propertyInfo.isPk();
            if(pk){
                count++;
                if(count==1){
                    pkStr.append(" WHERE ");
                }else{
                    pkStr.append(" AND ");
                }
                String dbColumnName = propertyInfo.getDbColumnName();
                String propertyName = propertyInfo.getPropertyName();
                pkStr.append(dbColumnName).append("=").append("#{").append(propertyName).append("}");
            }
        }

        return pkStr.toString();
    }

    public static void main(String[] args) {
        String str = "SELECT MIN(${max}) FROM";
        System.out.println(str.replaceAll("\\$\\{max\\}", "\\${coulumne}"));
    }
}
