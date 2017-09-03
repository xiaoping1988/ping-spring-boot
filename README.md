# ping-spring-boot

> 这是一个spring boot和mybatis、swagger集成的框架

## 帮助文档 示例项目:pring-spring-boot-sample-mybatis

### spring boot和mybatis集成

maven引入如下starter

    <dependency>
       <groupId>com.github.xiaoping1988.spring.boot</groupId>
       <artifactId>mybatis-ping-spring-boot-starter</artifactId>
       <version>1.0.1</version>
    </dependency>
    
此框架实现了单表的增删改查等90%左右的功能,支持mysql和oracle数据库

实体类规范:

必须继承BaseModel或者BaseTimeModel类
BaseTimeModel已继承BaseModel类，此类有两个时间数据属性create_time(创建时间)和update_time(修改时间)，实体对应的数据库表必须要有此二字段,serivce通过调用基类方法会自动更新此二字段。
所有的属性名称与数据库表字段名称保持一致

实体类的注解:

@Table: 数据库表名

@OrderBy: 排序

@Pk: 是否是主键,支持联合主键

@AutoIncrement: 如果是单一自增主键,务必在属性加上此注解

@Sequence: 如果是oracle数据库,自增主键的属性上务必加上此属性,标明自增序列

Dao层规范:

实体对应的dao必须在"实体包路径.dao"包路径下,名称必须为"实体名Dao",必须继承BaseCURDDao或者BaseQueryDao,前者支持增删改查,后者只支持查,已办视图对应的实体类Dao继承只读的Dao.

Service层规范:

必须继承BaseCURDService或者BaseQueryService,此二类已实现单表增删改查的大部分方法

自定义实体Dao的方法：

mapper文件位置在resources/mapper/,mapper文件的namespace必须是dao全路径名称,剩下的就跟平常写mybatis一样,id不能与基类dao中的方法名一样

如果想自定义一个跟实体无关的Dao,只要继承SqlMapper就行

具体可参考sample项目中的pring-spring-boot-sample-mybatis


### spring boot 集成swagger

maven引入如下starter

    <dependency>
        <groupId>com.github.xiaoping1988.spring.boot</groupId>
        <artifactId>swagger-ping-spring-boot-starter</artifactId>
        <version>1.0.1</version>
    </dependency>
    
yml配置:

swagger.enable: 开启或者关闭swagger,默认为true开启

一般在生产环境关闭swagger

集成之后,就可使用swagger注解来配置接口文档,文档查看地址:http://localhost:8080/swagger-ui.html

具体可参考sample项目中的pring-spring-boot-sample-mybatis
