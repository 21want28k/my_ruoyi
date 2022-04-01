package com.ruoyi.common.core.constant;

/**
 * Redis中的Token缓存的key 常量
 * 
 * @author ruoyi
 */
public class CacheConstants
{
    /**
     * Token缓存有效期，默认720（分钟）
     */
    public final static long EXPIRATION = 720;

    /**
     * Token缓存刷新时间，默认120（分钟）
     */
    public final static long REFRESH_TIME = 120;

    /**
     * 权限缓存key的前缀
     */
    public final static String LOGIN_TOKEN_KEY = "login_tokens:";
}
