package com.modeling.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.modeling.model.entity.Record;
import org.apache.ibatis.annotations.Mapper;

/**
 * (Record)表数据库访问层
 *
 * @author zrx
 * @since 2024-05-02 17:56:15
 */
@Mapper
public interface RecordMapper extends BaseMapper<Record> {

}
