package com.mybatis.ping.spring.boot.autoconfigure;

import com.mybatis.ping.spring.boot.MybatisProperties;
import com.mybatis.ping.spring.boot.core.PingSqlSessionFactoryBean;
import com.mybatis.ping.spring.boot.extend.service.MybatisQueryService;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigurationPackages;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.util.CollectionUtils;

import javax.sql.DataSource;
import java.util.List;


/**
 * mybatis加载
 * Created by 刘江平 on 2016-10-14.
 */
@Configuration
@ConditionalOnClass({SqlSessionFactory.class, PingSqlSessionFactoryBean.class, MybatisProperties.class})
@EnableConfigurationProperties({MybatisProperties.class})
@AutoConfigureAfter(DataSourceAutoConfiguration.class)
public class MybatisConfiguration {

    @Autowired
    private MybatisProperties mybatisProperties;

    @Autowired
    private BeanFactory beanFactory;

    @Bean
    @Primary
    @ConfigurationProperties(prefix = "spring.datasource")
    public DataSource primaryDataSource(DataSourceProperties dataSourceProperties) {
        DataSource dataSource = DataSourceBuilder.create()
                .driverClassName(dataSourceProperties.getDriverClassName())
                .url(dataSourceProperties.getUrl())
                .username(dataSourceProperties.getUsername())
                .password(dataSourceProperties.getPassword())
                .build();
        return dataSource;
    }

    @Bean
    @Primary
    @ConditionalOnMissingBean
    public SqlSessionFactory sqlSessionFactory(DataSource dataSource, DataSourceProperties dataSourceProperties) throws Exception {
        if (StringUtils.isBlank(this.mybatisProperties.getBasePackage())) {
            List<String> pkgs = AutoConfigurationPackages.get(beanFactory);
            if (!CollectionUtils.isEmpty(pkgs)) {
                this.mybatisProperties.setBasePackage(pkgs.get(0));
            }
        }
        return new PingSqlSessionFactoryBean(this.mybatisProperties, dataSource, dataSourceProperties).getObject();
    }


    @Bean
    @Primary
    @ConditionalOnMissingBean
    public SqlSessionTemplate sqlSessionTemplate(SqlSessionFactory sqlSessionFactory) {
        return new SqlSessionTemplate(sqlSessionFactory);
    }


    @Bean
    @Primary
    @ConditionalOnMissingBean
    public JdbcTemplate jdbcTemplate(DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }

    @Bean
    @Primary
    public MybatisQueryService mybatisQueryService(SqlSessionTemplate sqlSessionTemplate) {
        return new MybatisQueryService(sqlSessionTemplate);
    }

}
