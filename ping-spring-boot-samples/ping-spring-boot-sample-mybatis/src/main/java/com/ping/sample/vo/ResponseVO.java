package com.ping.sample.vo;


import com.mybatis.ping.spring.boot.extend.entity.ToStringModel;

/**
 * Created by liujiangping on 2017/8/20.
 */
public class ResponseVO<T> extends ToStringModel {

    private int code;

    private String msg;

    private T data;

    public ResponseVO() {}

    public ResponseVO(int code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
