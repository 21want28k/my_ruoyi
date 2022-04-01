package com.ruoyi.common.core.web.domain;

import com.ruoyi.common.core.constant.HttpStatus;

import java.util.HashMap;

public class AjaxResult extends HashMap<String, Object> {

    private static final long serialVersionUID = -3884707207276691334L;

    public static String CODE = "code";

    public static String MESSAGE = "msg";

    public static String DATA = "data";

    public static String SUCCESSFUL_OPERATION = "操作成功";

    public static String FAILED_OPERATION = "操作失败 ";

    public AjaxResult() {
    }

    public AjaxResult(int code, String msg, Object data) {
        super.put(CODE, code);
        super.put(MESSAGE, msg);
        if (data != null) {
            super.put(DATA, data);
        }
    }

    public AjaxResult(int code, String msg) {
        super.put(CODE, code);
        super.put(MESSAGE, msg);
    }


    public static AjaxResult success() {
        return AjaxResult.success(SUCCESSFUL_OPERATION);
    }

    public static AjaxResult success(String message) {
        return AjaxResult.success(message, null);
    }

    public static AjaxResult success(String message, Object data) {
        return new AjaxResult(HttpStatus.SUCCESS, message, data);
    }

    public static AjaxResult error() {
        return AjaxResult.error(FAILED_OPERATION);
    }

    public static AjaxResult error(String message) {
        return AjaxResult.error(HttpStatus.ERROR, message, null);
    }

    public static AjaxResult error(String message, Object data) {
        return AjaxResult.error(HttpStatus.ERROR, message, data);
    }


    /**
     * error code is not only 500.
     *
     * @param code    status code
     * @param message tips
     * @return message object
     */
    public static AjaxResult error(int code, String message) {
        return AjaxResult.error(code, message, null);
    }

    public static AjaxResult error(int code, String message, Object data) {
        return new AjaxResult(code, message, data);
    }

    /**
     * 如果影响的行数>0的话，就返回成功，不然就返回失败。
     *
     * @param rows 库中被影响的行数
     * @return
     */
    public static AjaxResult successIfAffectedRowsGreaterThan0(int rows) {
        return rows > 0 ? success() : error();
    }
}