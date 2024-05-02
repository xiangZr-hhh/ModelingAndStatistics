package com.modeling.model.entity;

import java.util.Date;
import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.IdType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
/**
 * (DataTable)表实体类
 *
 * @author zrx
 * @since 2024-04-30 15:48:29
 */
@SuppressWarnings("serial")
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("data_table")
public class DataTable  {

    @TableId(type= IdType.AUTO)
    private Long id;
    
    private String name;
    
    private String remark;
    
    private Integer filedTotal;
    
    private Long createdBy;
    
    private Date createdTime;
    
    private Long updatedBy;
    
    private Date updatedTime;

}
