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
 * (DataTable)表实体类
 *
 * @author zrx
 * @since 2024-05-04 13:52:23
 */
@SuppressWarnings("serial")
@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
@TableName("data_table")
public class DataTable {
    @TableId(type= IdType.AUTO)
    private Long id;
    //名称
    private String name;
    //备注
    private String remark;
    //列总数
    private Integer filedTotal;
    //数据总行数
    private Integer dataTotal;
    //创建者
    private Long createdBy;
    //创建时间
    private Date createdTime;
    //更新者
    private Long updatedBy;
    //更新时间
    private Date updatedTime;


}
