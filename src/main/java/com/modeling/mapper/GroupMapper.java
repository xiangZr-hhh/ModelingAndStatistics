package com.modeling.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.modeling.model.entity.Group;
import org.apache.ibatis.annotations.Mapper;

/**
 * (Group)表数据库访问层
 *
 * @author zrx
 * @since 2024-04-30 20:37:53
 */
@Mapper
public interface GroupMapper extends BaseMapper<Group> {

}
