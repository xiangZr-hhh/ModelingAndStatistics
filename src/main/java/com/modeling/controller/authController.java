package com.modeling.controller;

import com.modeling.model.vodata.UserLoginVO;
import com.modeling.model.vodata.UserRegisterVO;
import com.modeling.service.AuthService;
import com.modeling.utils.BaseResponse;
import com.modeling.utils.ErrorCode;
import com.modeling.utils.Processing;
import com.modeling.utils.ResultUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotNull;

/**
 * 描述：用户认证的控制器
 *
 * @author zrx
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("auth")
public class authController {

    private final AuthService authService;


    /**
     * 用户注册接口
     *
     * @param userRegisterVO 用户注册VO类
     * @param bindingResult  参数校验
     * @return com.modeling.utils.BaseResponse
     * @author zrx
     **/
    @PostMapping("register")
    public BaseResponse register(@RequestBody @Validated UserRegisterVO userRegisterVO,
                                 @NotNull BindingResult bindingResult) {
        // 判断是否有参数错误
        if (bindingResult.hasErrors()) {
            return ResultUtil.error(ErrorCode.REQUEST_BODY_ERROR, Processing.getValidatedErrorList(bindingResult));
        }

        return authService.authRegister(userRegisterVO);
    }


    /**
     *  用户登录接口
     *
     * @param userLoginVO 用户登录检验类
     * @param bindingResult 参数校验
     * @return com.modeling.utils.BaseResponse
     * @author zrx
     **/
    @PostMapping("login")
    public BaseResponse login(@RequestBody @Validated UserLoginVO userLoginVO,
                              @NotNull BindingResult bindingResult) {
        // 判断是否有参数错误
        if (bindingResult.hasErrors()) {
            return ResultUtil.error(ErrorCode.REQUEST_BODY_ERROR, Processing.getValidatedErrorList(bindingResult));
        }

        return authService.authLogin(userLoginVO);
    }



    /**
     *  用户退出登录
     *
     * @param request  请求
     * @return com.modeling.utils.BaseResponse
     * @author zrx
     **/
    @PostMapping("logout")
    public BaseResponse authLogout(HttpServletRequest request) {
        return authService.authLogout(request);
    }




}


