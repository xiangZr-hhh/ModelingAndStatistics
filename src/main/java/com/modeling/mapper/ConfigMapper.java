package com.modeling.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.modeling.model.entity.Config;
import org.apache.ibatis.annotations.Mapper;

/**
 * (Config)表数据库访问层
 *
 * @author zrx
 * @since 2024-04-30 20:37:52
 */
@Mapper
public interface ConfigMapper extends BaseMapper<Config> {

}
