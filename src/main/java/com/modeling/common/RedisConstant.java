package com.modeling.common;


/**
 * <h1>Redis常量类</h1>
 * <hr/>
 * 用于存放Redis常量
 *
 * @author zrx
 */
public class RedisConstant {
    /*
     * 类型分类
     */
    // 登陆相关
    public static final String TYPE_AUTH = "auth:";
    // 权限相关
    public static final String TYPE_PERMISSION = "permission:";

    /*
     * 表分类
     */
    // 邮箱验证码
    public static final String TABLE_EMAIL = "code:";
    // 令牌相关
    public static final String TABLE_TOKEN = "token:";
    // 用户相关
    public static final String TABLE_USER = "user:";
    // 角色相关
    public static final String TABLE_ROLE = "role:";
}
