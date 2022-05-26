package com.ruoyi.common.log.annotation;

import com.ruoyi.common.core.utils.StringUtils;
import com.ruoyi.common.log.enums.BusinessType;
import com.ruoyi.common.log.enums.OperatorType;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.PARAMETER})
@Documented
public @interface Log {
    /**
     * 模块名称
     */
    public String title() default StringUtils.EMPTY;

    /**
     * 功能
     */
    public BusinessType businessType() default BusinessType.OTHER;


    /**
     * 操作人类别
     */
    public OperatorType operatorType() default OperatorType.MANAGE;

    /**
     * 是否保存请求的参数
     */
    public boolean isSaveRequestData() default true;

    /**
     * 是否保存响应的参数
     */
    public boolean isSaveResponseData() default true;
}
