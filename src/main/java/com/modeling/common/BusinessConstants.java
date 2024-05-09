package com.modeling.common;

import lombok.Getter;

/**
 * <h1>业务常量</h1>
 * <hr/>
 * 业务常量
 *
 * @author xiao_lfeng
 * @version v1.1.0
 * @since v1.1.0
 */
@Getter
public enum BusinessConstants {
    CAPTCHA("captcha","验证码"),
    BUSINESS_LOGIN("login:", "登陆实现"),
    ALL_PERMISSION("all:", "所有权限"),
    USER("user:", "用户"),
    NONE("", "null");

    private final String value;
    private final String description;

    /**
     * 创建一个具有指定值和描述的实例 {@code BusinessConstants}。
     *
     * @param value       常量的value
     * @param description 常量的描述
     */
    BusinessConstants(String value, String description) {
        this.value = value;
        this.description = description;
    }
}
