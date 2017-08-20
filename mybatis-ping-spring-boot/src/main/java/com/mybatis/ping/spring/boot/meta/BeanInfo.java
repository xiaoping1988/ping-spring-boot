package com.mybatis.ping.spring.boot.meta;

import com.mybatis.ping.spring.boot.annotation.*;
import com.mybatis.ping.spring.boot.utils.ClassUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.util.ReflectionUtils;

import java.beans.PropertyDescriptor;
import java.beans.Transient;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;

/**
 * 数据库实体元信息
 * Created by 刘江平 on 2016-10-13.
 */
public class BeanInfo {
    /**
     * 实体对应数据库表名
     */
    private String tableName;
    /**
     * 实体全路径名称
     */
    private String entityFullClassName;
    /**
     * 实体类简称
     */
    private String entityName;
    /**
     * 实体对应的dao的全路径名称
     */
    private String entityDaoFullClassName;
    /**
     * 实体class
     */
    private Class<?> clazz;
    /**
     * 实体的所有字段信息,包含主键
     */
    private List<PropertyInfo> propertyInfos;
    /**
     * 主键字段信息
     */
    private List<PropertyInfo> pkPropertyInfos = new ArrayList<>();
    /**
     * 实体的字段信息
     */
    private Map<String,PropertyInfo> propertyInfosMap = new LinkedHashMap<String, PropertyInfo>();

    public BeanInfo(Class<?> clazz){
        this.clazz = clazz;
        this.entityFullClassName = clazz.getName();
        this.entityName = clazz.getSimpleName();
        String entityPackage = entityFullClassName.substring(0, entityFullClassName.lastIndexOf("."));
        String entitySimpleClassName = entityFullClassName.substring(entityFullClassName.lastIndexOf(".")+1);
        this.entityDaoFullClassName =entityPackage+".dao."+entitySimpleClassName+ "Dao";
        Table table = clazz.getAnnotation(Table.class);
        if(table!=null){
            this.tableName = table.value();
        }else{
            this.tableName = clazz.getSimpleName().toLowerCase();
        }
        this.propertyInfos = this.getPropertyInfos(clazz);
        for(PropertyInfo prop : propertyInfos){
            propertyInfosMap.put(prop.getPropertyName(), prop);
        }
    }

    /**
     * 获取某个实体类的属性元信息
     * @param clazz
     * @return
     */
    private <T> List<PropertyInfo> getPropertyInfos(Class<T> clazz){
        final List<Field> fields = new ArrayList<Field>();
        List<PropertyInfo> propertyInfos = new ArrayList<PropertyInfo>();
        //获取类所有的Fields
        ReflectionUtils.doWithFields(clazz, new ReflectionUtils.FieldCallback() {
            public void doWith(Field field) throws IllegalArgumentException,
                    IllegalAccessException {
                fields.add(field);
            }
        });
        for(Field field : fields){
            String fieldName = field.getName();
            PropertyDescriptor prop = BeanUtils.getPropertyDescriptor(clazz, fieldName);
            if(prop == null) continue;
            Method readMethod = prop.getReadMethod();
            Method writeMethod = prop.getWriteMethod();
            if(readMethod == null || writeMethod ==null) continue;
            if(!readMethod.isAnnotationPresent(Transient.class) && !isCollectionType(readMethod)){
                PropertyInfo propertyInfo = new PropertyInfo();
                propertyInfo.setField(field);
                String propertyName = ClassUtils.getPropertyName(readMethod);
                propertyInfo.setPropertyName(propertyName);
                propertyInfo.setReadMethod(readMethod);
                propertyInfo.setReturnType(readMethod.getReturnType());
                Column column = field.getAnnotation(Column.class);
                if(column!=null){
                    propertyInfo.setDbColumnName(column.value());
                }else{
                    propertyInfo.setDbColumnName(propertyName);
                }
                OrderBy orderBy = field.getAnnotation(OrderBy.class);
                if(orderBy!=null){
                    propertyInfo.setOrderBy(orderBy.value().toString());
                }
                AutoIncrement increment = field.getAnnotation(AutoIncrement.class);
                if(increment!=null){
                    propertyInfo.setIncrement(true);
                }
                Pk pk = field.getAnnotation(Pk.class);
                if(pk!=null){
                    propertyInfo.setPk(true);
                    Sequence sequence = field.getAnnotation(Sequence.class);
                    if(sequence!=null){
                        String seq_name = sequence.value();
                        if(StringUtils.isNotBlank(seq_name)){
                            propertyInfo.setSequence(seq_name);
                        }
                    }
                    this.pkPropertyInfos.add(propertyInfo);
                }
                propertyInfos.add(propertyInfo);
            }
        }
        /**将此结果集设置成不可修改*/
        propertyInfos = Collections.unmodifiableList(propertyInfos);
        return propertyInfos;

    }

    /**
     * 是否是集合类型
     * @param m
     * @return
     */
    private static boolean isCollectionType(Method m){
        boolean result = false;
//        if(m.getReturnType().isPrimitive())
//            return true;
        if(Collection.class.isAssignableFrom(m.getReturnType())){
            result = true;
        }else if(Map.class.isAssignableFrom(m.getReturnType())){
            result = true;
        }
        return result;
    }

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public String getEntityFullClassName() {
		return entityFullClassName;
	}

	public void setEntityFullClassName(String entityFullClassName) {
		this.entityFullClassName = entityFullClassName;
	}

	public String getEntityDaoFullClassName() {
		return entityDaoFullClassName;
	}

	public void setEntityDaoFullClassName(String entityDaoFullClassName) {
		this.entityDaoFullClassName = entityDaoFullClassName;
	}

	public List<PropertyInfo> getPropertyInfos() {
		return propertyInfos;
	}

	public void setPropertyInfos(List<PropertyInfo> propertyInfos) {
		this.propertyInfos = propertyInfos;
	}

	public List<PropertyInfo> getPkPropertyInfos() {
		return pkPropertyInfos;
	}

	public void setPkPropertyInfos(List<PropertyInfo> pkPropertyInfos) {
		this.pkPropertyInfos = pkPropertyInfos;
	}

	public Map<String, PropertyInfo> getPropertyInfosMap() {
		return propertyInfosMap;
	}

	public void setPropertyInfosMap(Map<String, PropertyInfo> propertyInfosMap) {
		this.propertyInfosMap = propertyInfosMap;
	}

	public Class<?> getClazz() {
		return clazz;
	}

	public void setClazz(Class<?> clazz) {
		this.clazz = clazz;
	}

	public String getEntityName() {
		return entityName;
	}

	public void setEntityName(String entityName) {
		this.entityName = entityName;
	}
}
