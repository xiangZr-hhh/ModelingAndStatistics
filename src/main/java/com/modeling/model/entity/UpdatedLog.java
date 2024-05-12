package com.modeling.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * (UpdatedLog)表实体类
 *
 * @author zrx
 * @since 2024-05-12 18:44:44
 */
@SuppressWarnings("serial")
@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
@TableName("updated_log")
public class UpdatedLog {
    @TableId(type= IdType.AUTO)
    private Long id;
    //内容
    private String content;
    //模块名称
    private String moduleName;
    
    private Long createdBy;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createdTime;
    //类型
    private String type;
    //版本
    private String version;
    //ip地址
    private String ip;


}
