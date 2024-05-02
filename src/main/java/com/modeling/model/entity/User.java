package com.modeling.model.entity;

import java.util.Date;
import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.IdType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.experimental.Accessors;

/**
 * (User)表实体类
 *
 * @author zrx
 * @since 2024-04-30 20:37:54
 */
@SuppressWarnings("serial")
@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
@TableName("user")
public class User {
    @TableId(type = IdType.AUTO)
    private Long id;
    //用户名
    private String username;
    //密码
    private String password;
    //昵称
    private String nick;
    //权限
    private Integer role;

    private Date createdTime;
    //是否删除(0:未删除；1已删除)
    private Integer isDelete;


}
