package com.modeling.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.modeling.model.entity.ModelingFile;
import org.apache.ibatis.annotations.Mapper;

/**
 * (ModelingFile)表数据库访问层
 *
 * @author zrx
 * @since 2024-04-30 20:37:53
 */
@Mapper
public interface ModelingFileMapper extends BaseMapper<ModelingFile> {

}
