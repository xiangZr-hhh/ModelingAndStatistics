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
 * (ModelingFile)表实体类
 *
 * @author zrx
 * @since 2024-04-30 20:37:53
 */
@SuppressWarnings("serial")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@TableName("modeling_file")
public class ModelingFile {
    @TableId(type = IdType.AUTO)
    private Long id;
    //所属文件夹id
    private Long directoryId;
    //密匙
    private String key;
    //文件名称
    private String name;
    //文件路径
    private String url;
    //文件类型
    private String type;
    //创建者用户id
    private Long createdBy;
    //创建时间
    private Date createdTime;
    //更新者用户id
    private Long updatedBy;
    //更新时间
    private Date updateTime;
    //是否删除（0：未删除；1：已删除）
    private Object isDelete;


}
