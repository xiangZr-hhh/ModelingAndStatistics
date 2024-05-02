package com.modeling.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.modeling.model.entity.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * (User)表数据库访问层
 *
 * @author zrx
 * @since 2024-04-30 20:37:54
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {

}
