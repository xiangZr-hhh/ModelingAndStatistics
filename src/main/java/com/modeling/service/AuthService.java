package com.modeling.service;

import com.modeling.model.vodata.UserLoginVO;
import com.modeling.model.vodata.UserRegisterVO;
import com.modeling.utils.BaseResponse;

/**
 * 描述：用户登录服务层接口
 *
 * @author zrx
 **/
public interface AuthService {

    BaseResponse authLogin(UserLoginVO userLoginVO);

    BaseResponse authRegister(UserRegisterVO userRegisterVO);
}
