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
 * (Execute)表实体类
 *
 * @author zrx
 * @since 2024-04-30 22:44:10
 */
@SuppressWarnings("serial")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@TableName("execute")
public class Execute  {
    @TableId(type= IdType.AUTO)
    private Long id;
    //操作的数据表id
    private Long tableId;
    //类型
    private String type;
    //执行文件
    private Long fileId;
    
    private Long createdBy;
    
    private Date createdTime;
    //是否删除（0：未删除；1：已删除）
    private Integer isDelete;
    //执行结果（0：失败；1：正常；2：回退）
    private Integer result;


}
