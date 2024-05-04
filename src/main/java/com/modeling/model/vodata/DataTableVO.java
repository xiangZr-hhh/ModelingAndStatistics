package com.modeling.model.vodata;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.modeling.model.entity.Filed;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;
import java.util.List;

/**
 * 描述：
 *
 * @author zrx
 */
@Data
@Accessors(chain = true)
public class DataTableVO {

    private Long id;

    private String name;

    private String remark;

    private Integer filedTotal;

    private Integer dataTotal;

    private List<FiledDataVO> fileds;

    private Long createdBy;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createdTime;
}


