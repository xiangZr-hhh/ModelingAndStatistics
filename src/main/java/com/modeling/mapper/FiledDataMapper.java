package com.modeling.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.modeling.model.entity.FiledData;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * (FiledData)表数据库访问层
 *
 * @author zrx
 * @since 2024-04-30 20:45:16
 */
@Mapper
public interface FiledDataMapper extends BaseMapper<FiledData> {

    void insertBatchSomeColumn(@Param("list") List<FiledData> filedDataList);

}
