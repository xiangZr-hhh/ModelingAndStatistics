package com.modeling.model.vodata;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

/**
 * 描述：用户登录VO封装类
 *
 * @author zrx
 */
@Data
public class UserLoginVO {

    @Pattern(regexp = "^[0-9A-Za-z_]+$")
    @NotBlank(message = "用户名不能为空")
    private String username;
    @NotBlank(message = "密码不能为空")
    private String password;

}


