package com.mybatis.ping.spring.boot.extend.entity;

import com.alibaba.fastjson.JSON;

/**
 * 重写toString方法
 * Created by liujiangping on 2017/8/20.
 */
public class ToStringModel {
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(this.getClass().getSimpleName())
                .append(":")
                .append(JSON.toJSONString(this));
        return sb.toString();
    }
}
