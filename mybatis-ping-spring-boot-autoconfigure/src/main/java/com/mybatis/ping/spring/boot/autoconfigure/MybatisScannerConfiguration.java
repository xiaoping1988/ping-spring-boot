package com.mybatis.ping.spring.boot.autoconfigure;

import com.mybatis.ping.spring.boot.extend.dao.SqlMapper;
import org.apache.ibatis.annotations.Mapper;
import org.mybatis.spring.mapper.MapperScannerConfigurer;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.boot.autoconfigure.AutoConfigurationPackages;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * Created by liujiangping on 2017/8/20.
 */
@Configuration
public class MybatisScannerConfiguration implements BeanFactoryAware {

    private BeanFactory beanFactory;

    @Bean
    public MapperScannerConfigurer mapperScannerConfigurer() {
        List<String> pkgs = AutoConfigurationPackages.get(beanFactory);
        MapperScannerConfigurer mapperScannerConfigurer = new MapperScannerConfigurer();
        mapperScannerConfigurer.setBasePackage(pkgs.get(0));
        mapperScannerConfigurer.setMarkerInterface(SqlMapper.class);
        mapperScannerConfigurer.setAnnotationClass(Mapper.class);
        return mapperScannerConfigurer;
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }

}
