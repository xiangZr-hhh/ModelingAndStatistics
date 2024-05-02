package com.modeling.model.vodata;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;
import java.util.List;

/**
 * 描述：数据表分页VO封装类
 *
 * @author zrx
 */
public class DataTablePageVO {

    private Long id;

    private String name;

    private String remark;

    private Integer filedTotal;

    private List<FiledDataVO> fileds;

    private Long createdBy;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createdTime;
}


