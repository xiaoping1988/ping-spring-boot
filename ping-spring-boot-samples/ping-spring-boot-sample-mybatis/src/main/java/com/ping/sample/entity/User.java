package com.ping.sample.entity;

import com.mybatis.ping.spring.boot.annotation.AutoIncrement;
import com.mybatis.ping.spring.boot.annotation.Pk;
import com.mybatis.ping.spring.boot.annotation.Table;
import com.mybatis.ping.spring.boot.extend.entity.BaseTimeModel;
import org.springframework.util.Assert;

/**
 * 用户表,字段与数据库一一对应即可,
 * Created by liujiangping on 2017/8/20.
 */
@Table("sys_user")
public class User extends BaseTimeModel {

    /**
     * 主键自增
     */
    @Pk
    @AutoIncrement
    private Long user_id;

    private String user_name;

    private String user_account;

    private Integer user_age;

    private String user_city;

    public User () {}

    public User(Long user_id) {
        Assert.notNull(user_id, "user_id can not be null!");
        this.user_id = user_id;
    }

    public Long getUser_id() {
        return user_id;
    }

    public void setUser_id(Long user_id) {
        this.user_id = user_id;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getUser_account() {
        return user_account;
    }

    public void setUser_account(String user_account) {
        this.user_account = user_account;
    }

    public Integer getUser_age() {
        return user_age;
    }

    public void setUser_age(Integer user_age) {
        this.user_age = user_age;
    }

    public String getUser_city() {
        return user_city;
    }

    public void setUser_city(String user_city) {
        this.user_city = user_city;
    }
}
