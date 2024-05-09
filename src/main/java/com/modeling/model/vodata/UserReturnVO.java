package com.modeling.model.vodata;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;
import java.util.List;

/**
 * 描述：
 *
 * @author zrx
 */
@Data
@Accessors(chain = true)
public class UserReturnVO {

    private Long id;
    //用户名
    private String username;
    //昵称
    private String nick;
    //权限
    private Integer role;
    //权限名称
    private List<String> roleName;
    //token
    private String token;

}


