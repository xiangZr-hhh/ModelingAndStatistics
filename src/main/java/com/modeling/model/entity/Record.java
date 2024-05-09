package com.modeling.model.entity;

import java.util.Date;
import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.IdType;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.experimental.Accessors;

/**
 * (Record)表实体类
 *
 * @author zrx
 * @since 2024-05-02 17:56:15
 */
@SuppressWarnings("serial")
@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
@TableName("record")
public class Record  {
    @TableId(type= IdType.AUTO)
    private Long id;
    
    private String type;
    
    private String content;
    
    private Long userId;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createdTime;
    
    private String ip;


}
