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
 * (Group)表实体类
 *
 * @author zrx
 * @since 2024-04-30 20:37:53
 */
@SuppressWarnings("serial")
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("group")
public class Group {
    @TableId(type = IdType.AUTO)
    private Long id;

    private String name;
    //负责人
    private Long responsible;
    //创建者用户id
    private Long createdBy;
    //创建时间
    private Date createdTime;
    //更新者用户id
    private Long updatedBy;
    //更新时间
    private Date updatedTime;


}
