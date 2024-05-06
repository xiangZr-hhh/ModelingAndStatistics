package com.modeling.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.modeling.model.entity.User;
import com.modeling.utils.BaseResponse;

import javax.servlet.http.HttpServletRequest;


/**
 * (User)表服务接口
 *
 * @author zrx
 * @since 2024-04-30 20:37:54
 */
public interface UserService extends IService<User> {

    BaseResponse getUserByUsername(HttpServletRequest request);
}

