package com.mybatis.ping.spring.boot.meta;

import com.mybatis.ping.spring.boot.enums.DbDialect;
import com.mybatis.ping.spring.boot.extend.entity.BaseModel;
import com.mybatis.ping.spring.boot.extend.entity.BaseTimeModel;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.util.*;


/**
 *  项目元数据信息
 * Created by 刘江平 on 2016-10-13.
 */
public class BaseMeta {
    private static final Logger logger = LoggerFactory.getLogger(BaseMeta.class);
    /**
     * 项目的数据库系统,默认MYSQL
     */
    public static DbDialect db = DbDialect.MYSQL;

    /**所有数据库实体类的map集合,key是实体类的class*/
    private static Map<Class<?>, BeanInfo> beanInfoMappings = new HashMap<Class<?>, BeanInfo>();

    /**所有数据库实体类的map集合,key是实体类的SimpleClassName*/
    private static Map<String, BeanInfo> beanInfoMappingsBySimpleClassName = new HashMap<String, BeanInfo>();

    /**实体类简称对应的包名,key是实体类的SimpleClassName,value是该实体所在的包名*/
    private static Map<String,String> simpleClassNameMappingPackage = new HashMap<>();

    /**
     * 根据实体的类得到实体的元数据信息
     * @param clazz 数据库实体类class
     * @return
     */
    public static <T> BeanInfo getBeanInfo(Class<T> clazz){
        return beanInfoMappings.get(clazz);
    }
    /**
     * 根据实体简称得到实体的元数据信息
     * @param simpleClassName
     * @return
     */
    public static BeanInfo getBeanInfo(String simpleClassName){
    	return beanInfoMappingsBySimpleClassName.get(simpleClassName);
    }
    /**
     * 根据实体的类得到实体的元数据信息,并放进内存中
     * @param clazz 数据库实体类class
     * @return 返回实体的类得到实体的元数据对象
     */
    public static <T> BeanInfo putBeanInfo(Class<T> clazz){
        BeanInfo beanInfo = beanInfoMappings.get(clazz);
        if(beanInfo == null){
            String simpleClassName = clazz.getSimpleName();
            String className = clazz.getName();
            if(beanInfoMappingsBySimpleClassName.containsKey(simpleClassName)){
                throw new RuntimeException("数据库实体"+beanInfoMappingsBySimpleClassName.get(simpleClassName).getClass().getName()+"与"+className+"的simpleClassName冲突!!!");
            }
            beanInfo = new BeanInfo(clazz);
            beanInfoMappingsBySimpleClassName.put(simpleClassName, beanInfo);
            beanInfoMappings.put(clazz, beanInfo);
            simpleClassNameMappingPackage.put(simpleClassName,className.substring(0,className.lastIndexOf(".")));
        }
        return beanInfo;
    }
    /**
     *  获取所有的bean maps
     * @return
     */
    public static Map<Class<?>, BeanInfo> getBeanMaps(){
        return Collections.unmodifiableMap(beanInfoMappings);
    }

    /**
     * 获取特定接口bean maps
     * @param clazz 接口类
     * @return
     */
    public static Map<Class<?>, BeanInfo> getBeans(Class<?> clazz){
        Map<Class<?>, BeanInfo> result = new HashMap<Class<?>, BeanInfo>();
        for(Class<?> c : beanInfoMappings.keySet()){
            if(clazz.isAssignableFrom(c)){
                result.put(c, beanInfoMappings.get(c));
            }
        }
        return Collections.unmodifiableMap(result);
    }

    /**
     * 得到clazz实体对应的属性为propertyName的属性的元数据信息
     * @param clazz
     * @param propertyName
     * @return
     */
    public static <T> PropertyInfo getPropertyInfo(Class<T> clazz, String propertyName){
        BeanInfo beanInfo = beanInfoMappings.get(clazz);
        if(beanInfo != null){
            return beanInfo.getPropertyInfosMap().get(propertyName);
        }
        return null;
    }

    /**
     * 初始化项目实体元数据
     * @param basePackage
     * @param dataSourceProperties
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
	public static void init(String basePackage, DataSourceProperties dataSourceProperties){
        String jdbcUrl = dataSourceProperties.getUrl();
        if(StringUtils.isBlank(jdbcUrl)){
            throw new RuntimeException("spring.datasource.url属性没有值！！！");
        }
        if(jdbcUrl.toUpperCase().indexOf(DbDialect.MYSQL.toString())>=0){
            db = DbDialect.MYSQL;
        }else if(jdbcUrl.toUpperCase().indexOf(DbDialect.ORACLE.toString())>=0){
            db = DbDialect.ORACLE;
        }else{
            throw new RuntimeException("jdbcUrl不是"+ ArrayUtils.toString(DbDialect.values())+"中的数据库系统！！！");
        }
        logger.info("初始化项目实体元数据 start...");
        Set<Class> clazzs = doScan(basePackage);
        if(CollectionUtils.isEmpty(clazzs)){
            logger.info("没有继承类["+BaseModel.class.getName()+"]的实体!!!");
        }else{
            for (Class clazz:clazzs) {
                putBeanInfo(clazz);
            }
        }
        logger.info("初始化项目["+ clazzs.size()+"]个实体元数据 end...");
    }

    /**
     * 扫描包根路径下所有的class，筛选出继承了BaseModel的class资源
     * @param basePackages
     * @return
     */
    @SuppressWarnings("rawtypes")
	private static Set<Class> doScan(String ... basePackages) {
        Set<Class> clazzs = new HashSet<>();
        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver(
                Thread.currentThread().getContextClassLoader());
        MetadataReaderFactory metadataReaderFactory = new CachingMetadataReaderFactory(resolver);
        try {
            for (String bp : basePackages) {
            	if(StringUtils.isBlank(bp)){
            		continue;
            	}
            	String pkg = bp.replaceAll("\\.", "/");
				Resource[] rs = resolver.getResources("classpath*:"+pkg+"/**/*.class");
                for (Resource resource : rs) {
                    String className = metadataReaderFactory
                            .getMetadataReader(resource).getClassMetadata()
                            .getClassName();
                    Class<?> clazz;
                    try {
                        clazz = Class.forName(className);
                        if(BaseModel.class.isAssignableFrom(clazz)&&clazz!=BaseModel.class&&clazz!=BaseTimeModel.class){
                            clazzs.add(clazz);
                        }
                    } catch (Exception e) {
                    	logger.debug(ExceptionUtils.getStackTrace(e));
                    }
                }
			}
        	
        } catch (IOException e) {
            e.printStackTrace();
        }
        return clazzs;
    }

    public static void main(String[] args) {
        System.out.println(ArrayUtils.toString(DbDialect.values()));
    }
}
