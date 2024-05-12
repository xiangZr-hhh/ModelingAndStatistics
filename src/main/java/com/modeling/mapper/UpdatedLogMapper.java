package com.modeling.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.modeling.model.entity.UpdatedLog;
import org.apache.ibatis.annotations.Mapper;

/**
 * (UpdatedLog)表数据库访问层
 *
 * @author zrx
 * @since 2024-05-12 17:32:00
 */
@Mapper
public interface UpdatedLogMapper extends BaseMapper<UpdatedLog> {

}
