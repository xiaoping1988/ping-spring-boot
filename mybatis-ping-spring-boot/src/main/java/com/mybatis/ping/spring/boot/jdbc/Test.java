package com.mybatis.ping.spring.boot.jdbc;

import com.mybatis.ping.spring.boot.extend.entity.BaseModel;

public class Test extends BaseModel {
    private String name;
    private Integer age;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }
}
