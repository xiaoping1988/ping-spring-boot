package com.mybatis.ping.spring.boot.core;

import com.mybatis.ping.spring.boot.MybatisProperties;
import com.mybatis.ping.spring.boot.constant.MybatisConstant;
import com.mybatis.ping.spring.boot.meta.BaseMeta;
import com.mybatis.ping.spring.boot.meta.BeanInfo;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.plugin.Interceptor;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import javax.sql.DataSource;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.*;
import java.util.Map.Entry;


/**
 * 继承自SqlSessionFactoryBean类，实现Mapper基本操作模块化功能
 * 通过模板动态加载所有entity对应的mapper文件
 */
public class PingSqlSessionFactoryBean extends SqlSessionFactoryBean {
	private static final Logger logger = LoggerFactory.getLogger(PingSqlSessionFactoryBean.class);
	private ResourceLoader resourceLoader = new DefaultResourceLoader();

	private MybatisProperties mybatisProperties;

	public PingSqlSessionFactoryBean(MybatisProperties mybatisProperties, DataSource dataSource, DataSourceProperties dataSourceProperties){
		this.mybatisProperties = mybatisProperties;
		BaseMeta.init(mybatisProperties.getBasePackage(),dataSourceProperties);
		this.setDataSource(dataSource);
		Resource configResource = this.resolveConfigLocation();
		if(configResource!=null){
			this.setConfigLocation(configResource);
		}
		Resource[] mapperResources = this.resolveMapperLocations();
		this.setMapperLocations(mapperResources);
		this.setPlugins(new Interceptor[]{new PaginationInterceptor()});
	}

	/**
	 * 加载自定义mapper文件资源
	 * @return
	 */
	private Resource[] resolveMapperLocations() {
		String[] mapperLocations = this.mybatisProperties.getMapperLocations();
		if(mapperLocations==null||mapperLocations.length==0){
			mapperLocations = MybatisConstant.DEFAULT_MAPPER_LOCATIONS.split(",");
		}
		List<Resource> resources = new ArrayList<Resource>();
		for (String mapperLocation : mapperLocations) {
			Resource[] mappers;
			try {
				mappers = new PathMatchingResourcePatternResolver().getResources(mapperLocation);
				resources.addAll(Arrays.asList(mappers));
			} catch (IOException e) {

			}
		}

		Resource[] mapperResources = new Resource[resources.size()];
		mapperResources = resources.toArray(mapperResources);
		return mapperResources;
	}

	/**
	 * 加载mybatis的config文件
	 * @return
	 */
	private Resource resolveConfigLocation() {
		if(StringUtils.isNotBlank(this.mybatisProperties.getConfigLocation())){
			return new DefaultResourceLoader().getResource(this.mybatisProperties.getConfigLocation());
		}
		return null;
	}

	/**
	 * 在此方法中加入了动态mapper 文件插入
	 * @param mapperLocations  已静态写好的mapper资源文件列表
	 */
	@Override
	public void setMapperLocations(Resource[] mapperLocations) {
		try {
			//1.生成mapper资源文件对象
			List<MapperResource> resources = createEntitiesMapperResources();

			//2.将原始source和生成的source整合
			super.setMapperLocations(mergeMapperResources(mapperLocations,
					resources));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	/**
	 * 将自定义的mapper文件和common_mapper.xml加到动态生成的mapper文件中。返回合并后的resource集合。
	 * 
	 * @param sources
	 *            资源文件列表
	 * @param dynamicResources
	 *            动态生成的资源文件
	 * @return 添加后的资源
	 * @throws Exception
	 */
	private Resource[] mergeMapperResources(Resource[] sources,
											List<MapperResource> dynamicResources) throws Exception {
		List<Resource> mergedResources = new ArrayList<Resource>();
		//存储自定义的mapper文件资源
		Map<String, MapperResource> mapperMap = new HashMap<String, MapperResource>();
		// 把自定义的mapper文件按namespace进行拆分。目前namespace相同的mapper文件只能自定义到一个文件中去。
		for (Resource resource : sources) {
			String mapperText = IOUtils.toString(resource.getInputStream(), "utf-8");
			MapperResource mr = new MapperResource(mapperText, resource);
			mapperMap.put(mr.getNamespace(), mr);
		}
		// 将生成的动态mapper文件和自定义的mapper文件进行合并。
		for (MapperResource dynamicMapper : dynamicResources) {
			MapperResource mapper = mapperMap.get(dynamicMapper.getNamespace());
			if (mapper != null) {
				String finalMapper = dynamicMapper.getMapperText().replace(
						dynamicMapper.getSqlNodes(),
						dynamicMapper.getSqlNodes() + mapper.getSqlNodes());

				mapperMap.remove(mapper.getNamespace());
				mergedResources
						.add(new InputStreamResource(new ByteArrayInputStream(
								finalMapper.getBytes("UTF-8")), mapper
								.getNamespace()));
			} else { // 如果同名的nampespace下没有自定义的mapper文件，则加入动态生成的mapper文件
				mergedResources.add(new InputStreamResource(
						new ByteArrayInputStream(dynamicMapper.getMapperText()
								.getBytes("UTF-8")), dynamicMapper
								.getNamespace()));
			}
		}
		// 加入所有用户自定义的和动态生成合并后剩下的用户自定义的mapper文件。
		for (MapperResource mr : mapperMap.values()) {
			mergedResources.add(mr.getResource());
		}
		//加入common_mapper.xml
		mergedResources.add(this.resourceLoader.getResource(MybatisConstant.MYBATIS_COMMON_MAPPER));
		return mergedResources.toArray(new Resource[] {});

	}

	/**
	 * 将与数据库对应的所有entity生成mapper对应的基本文件
	 * @return
	 */
	private List<MapperResource> createEntitiesMapperResources() throws IOException {
		List<MapperResource> mappers = new ArrayList<MapperResource>();
		Map<Class<?>,BeanInfo> beanInfos = BaseMeta.getBeanMaps();
		for (Iterator<Entry<Class<?>,BeanInfo>> it = beanInfos.entrySet().iterator(); it.hasNext();) {
			Entry<Class<?>, BeanInfo> entry = it.next();
			mappers.add(MapperCRUDFactory.getInstance().buildMapper(entry.getValue()));
		}
		logger.info("依据模板生成的mapper资源文件个数["+mappers.size()+"]");
		return mappers;
	}

}
