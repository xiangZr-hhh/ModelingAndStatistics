package com.modeling.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * (Filed)表实体类
 *
 * @author zrx
 * @since 2024-05-04 16:44:15
 */
@SuppressWarnings("serial")
@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
@TableName("filed")
public class Filed {
    @TableId(type= IdType.AUTO)
    private Long id;
    
    private String name;
    //字段数值类型
    private String type;
    
    private Integer filedDataSize;
    //所属数据表id
    private Long tableId;
    
    private Date createdTime;


}
