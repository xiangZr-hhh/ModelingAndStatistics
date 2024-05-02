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
 * (Config)表实体类
 *
 * @author zrx
 * @since 2024-04-30 20:37:52
 */
@SuppressWarnings("serial")
@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
@TableName("config")
public class Config {
    @TableId(type = IdType.AUTO)
    private Integer id;

    private String name;

    private String data;

    private Date createTime;


}
