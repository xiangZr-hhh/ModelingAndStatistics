package com.modeling.model.entity;

import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.experimental.Accessors;

/**
 * (UpdatedLog)表实体类
 *
 * @author zrx
 * @since 2024-05-12 17:32:00
 */
@SuppressWarnings("serial")
@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
@TableName("updated_log")
public class UpdatedLog  {
    @TableId(type= IdType.AUTO)
    private Long id;
    
    private Long createdBy;
    
    private Date createdTime;
    //内容
    private String content;
    //类型
    private String type;
    //版本
    private String version;
    //ip地址
    private String ip;


}
