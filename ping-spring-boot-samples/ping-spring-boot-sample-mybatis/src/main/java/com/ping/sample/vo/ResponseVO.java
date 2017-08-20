package com.ping.sample.vo;


import com.mybatis.ping.spring.boot.extend.entity.ToStringModel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * Created by liujiangping on 2017/8/20.
 */
@ApiModel("返回结果")
public class ResponseVO<T> extends ToStringModel {

    @ApiModelProperty("状态码")
    private int code;

    @ApiModelProperty("提示信息")
    private String msg;

    @ApiModelProperty("具体数据")
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
