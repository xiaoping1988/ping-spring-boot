package com.mybatis.ping.spring.boot.utils;

import com.alibaba.fastjson.JSON;

import java.util.List;

/**
 * Created by liujiangping on 2017/3/15.
 */
public class ResponseJsonParser {

    public static <T> List<T> parserDataToList(String responseJson, Class<T> clazz){
        String data = JSON.parseObject(responseJson).getString("data");
        return JSON.parseArray(data,clazz);
    }

    public static <T> T parserDataToObject(String responseJson,Class<T> clazz){
        String data = JSON.parseObject(responseJson).getString("data");
        return JSON.parseObject(data,clazz);
    }

    public static long getPageTotalCount(String responseJson){
        String data = JSON.parseObject(responseJson).getString("data");
        return JSON.parseObject(data).getLong("totalCount");
    }
}
