package com.modeling.dao;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.modeling.mapper.UserMapper;
import com.modeling.model.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 描述：用于处理用户相关的请求
 *
 * @author zrx
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class UserDAO {

    private final UserMapper userMapper;

    private final LambdaQueryWrapper<User> userWrapper = new LambdaQueryWrapper<>();

    /**
     * 根据用户名查找用户，查找到多个用户或没有查找到，返回null
     *
     * @param username 用户名
     * @return com.modeling.dao.UserDAO
     * @author zrx
     **/
    public User getUserByUsername (String username) {
        userWrapper.clear();
        userWrapper.eq(User::getUsername,username)
                .eq(User::getIsDelete,0);
        List<User> users = userMapper.selectList(userWrapper);

        return users.size() == 1 ? users.get(0) : null;
    }

    /**
     * 判断用户是否存在，通过用户名
     *
     * @param username  用户名
     * @return boolean
     * @author zrx
     **/
    public boolean checkUserIsExistByUsername (String username) {
        userWrapper.clear();
        userWrapper.eq(User::getUsername,username)
                .eq(User::getIsDelete,0);
        List<User> users = userMapper.selectList(userWrapper);

        return users.size() >= 1 ;
    }


    /**
     * 根据用户id查询用户名称
     *
     * @param uid  用户id
     * @return java.lang.String
     * @author zrx
     **/
    public String getUserNameById (Long uid) {

        User user = userMapper.selectById(uid);

        if (user == null) {
            return "无此用户";
        }

        if (user.getNick() == null || user.getNick().equals("")) {
            return "暂无昵称 (用户名："+user.getUsername()+")";
        }

        return user.getNick()+"(用户名："+user.getUsername()+")";

    }


}


