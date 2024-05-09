package com.modeling.service;

import com.modeling.model.vodata.UserLoginVO;
import com.modeling.model.vodata.UserRegisterVO;
import com.modeling.utils.BaseResponse;

import javax.servlet.http.HttpServletRequest;

/**
 * 描述：用户登录服务层接口
 *
 * @author zrx
 **/
public interface AuthService {

    BaseResponse authLogin(UserLoginVO userLoginVO, HttpServletRequest request);

    BaseResponse authRegister(UserRegisterVO userRegisterVO);

    BaseResponse authLogout(HttpServletRequest request);
}
