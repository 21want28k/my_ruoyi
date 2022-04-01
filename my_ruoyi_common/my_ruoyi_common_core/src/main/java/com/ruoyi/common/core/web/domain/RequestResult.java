package com.ruoyi.common.core.web.domain;

import com.ruoyi.common.core.constant.Constants;
import com.ruoyi.common.core.constant.HttpStatus;

import java.io.Serializable;

public class RequestResult<T> implements Serializable {

    private static final long serialVersionUID = 634075888864028972L;

    private int code;

    private String msg;

    private T data;

    public RequestResult() {
    }

    public RequestResult(int code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public static void main(String[] args) {
        System.out.println(RequestResult.success("aaa"));
    }


    public static <T> RequestResult<T> success() {
        return RequestResult.success(Constants.OPERATION_SUCCESS_STRING);
    }

    public static <T> RequestResult<T> success(String msg) {
        return RequestResult.success(msg, null);
    }

    public static <T> RequestResult<T> success(T data) {
        return RequestResult.success(Constants.OPERATION_SUCCESS_STRING, data);
    }


    public static <T> RequestResult<T> success(String msg, T data) {
        return new RequestResult<>(HttpStatus.SUCCESS, msg, data);
    }

    public static <T> RequestResult<T> fail() {
        return RequestResult.success(Constants.OPERATION_FAIL_STRING);
    }

    public static <T> RequestResult<T> fail(String msg) {
        return RequestResult.fail(msg, null);
    }

    public static <T> RequestResult<T> fail(String msg, T data) {
        return new RequestResult<>(HttpStatus.ERROR, msg, data);
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
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

    @Override
    public String toString() {
        return "RequestResult{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                '}';
    }
}