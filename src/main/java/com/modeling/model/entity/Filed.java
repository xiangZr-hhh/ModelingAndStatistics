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
 * (Filed)表实体类
 *
 * @author zrx
 * @since 2024-04-30 20:37:52
 */
@SuppressWarnings("serial")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@TableName("filed")
public class Filed {
    @TableId(type = IdType.AUTO)
    private Long id;

    private String name;
    //字段数值类型
    private String type;
    //所属数据表id
    private Long tableId;

    private Date createdTime;


}
