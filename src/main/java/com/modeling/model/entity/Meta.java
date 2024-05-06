package com.modeling.model.entity;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.IdType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.experimental.Accessors;

/**
 * (Meta)表实体类
 *
 * @author zrx
 * @since 2024-05-04 21:12:34
 */
@SuppressWarnings("serial")
@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
@TableName("meta")
public class Meta  {
    @TableId(type= IdType.AUTO)
    private Long id;
    
    private Long pid;
    
    private String icon;
    
    private Object title;


}
