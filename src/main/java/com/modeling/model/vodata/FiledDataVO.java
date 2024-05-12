package com.modeling.model.vodata;

import com.fasterxml.jackson.annotation.JsonFormat;
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
public class FiledDataVO {

    private Long id;

    private String name;
    //字段数值类型
    private String type;
    //所属数据表id
    private Long tableId;

    private Integer filedDataSize;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createdTime;

    private List<String> excelData;
}


