package com.modeling.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.modeling.model.entity.Route;
import org.apache.ibatis.annotations.Mapper;

/**
 * (Route)表数据库访问层
 *
 * @author zrx
 * @since 2024-05-04 21:12:29
 */
@Mapper
public interface RouteMapper extends BaseMapper<Route> {

}
