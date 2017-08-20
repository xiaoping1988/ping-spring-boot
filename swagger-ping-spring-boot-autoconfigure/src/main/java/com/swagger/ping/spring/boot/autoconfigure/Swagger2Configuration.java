package com.swagger.ping.spring.boot.autoconfigure;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Created by 刘江平 on 2017-01-19.
 */
@Configuration
@ConditionalOnExpression("${swagger.enable:true}")
@EnableSwagger2
public class Swagger2Configuration {
    @Value("${spring.application.name}")
    private String applicationName;



    @Bean
    public Docket swaggerApi(){
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.regex("/api/.*"))
                .build()
                .apiInfo(apiInfo());
    }

    private ApiInfo apiInfo(){
        return  new ApiInfoBuilder()
                .title(this.applicationName)
                .description("应用["+this.applicationName+"]的接口文档")
                .build();
    }
}
