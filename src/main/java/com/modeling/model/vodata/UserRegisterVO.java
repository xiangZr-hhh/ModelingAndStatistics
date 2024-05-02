package com.modeling.model.vodata;

import lombok.Data;
import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

/**
 * 描述：
 *
 * @author zrx
 */
@Getter
public class UserRegisterVO {

    @NotBlank(message = "用户名不能为空")
    @Pattern(regexp = "^[0-9A-Za-z_]{3,40}$", message = "用户名只能为字母、数字或下划线")
    private String username;

    @NotBlank(message = "密码不能为空")
    private String password;
}


