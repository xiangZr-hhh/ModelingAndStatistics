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
 * (Route)表实体类
 *
 * @author zrx
 * @since 2024-05-04 21:12:57
 */
@SuppressWarnings("serial")
@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
@TableName("route")
public class Route  {
    @TableId(type= IdType.AUTO)
    private Long id;
    
    private Long parent;
    
    private String name;
    
    private String path;
    
    private String component;


}
