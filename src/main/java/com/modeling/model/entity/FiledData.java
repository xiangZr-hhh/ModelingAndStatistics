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
 * (FiledData)表实体类
 *
 * @author zrx
 * @since 2024-04-30 22:44:15
 */
@SuppressWarnings("serial")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@TableName("filed_data")
public class FiledData  {
    @TableId(type= IdType.AUTO)
    private Integer id;
    
    private String data;
    
    private Long filedId;
    
    private Long executeId;
    
    private Date createdTime;
    //是否删除（0：未删除；1：已删除）
    private Integer isDelete;


}
