package com.modeling.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.modeling.model.entity.Meta;
import org.apache.ibatis.annotations.Mapper;

/**
 * (Meta)表数据库访问层
 *
 * @author zrx
 * @since 2024-05-04 21:12:34
 */
@Mapper
public interface MetaMapper extends BaseMapper<Meta> {

}
