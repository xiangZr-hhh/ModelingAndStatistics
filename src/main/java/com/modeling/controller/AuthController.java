package com.modeling.controller;

import com.modeling.common.BusinessConstants;
import com.modeling.model.vodata.UserLoginVO;
import com.modeling.model.vodata.UserRegisterVO;
import com.modeling.service.AuthService;
import com.modeling.utils.*;
import com.modeling.utils.redis.CaptchaRedisUtil;
import com.wf.captcha.GifCaptcha;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * 描述：用户认证的控制器
 *
 * @author zrx
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("auth")
public class AuthController {

    private final AuthService authService;
    private final CaptchaRedisUtil captchaRedisUtil;

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
                              @NotNull BindingResult bindingResult,
                              HttpServletRequest request) {
        // 判断是否有参数错误
        if (bindingResult.hasErrors()) {
            return ResultUtil.error(ErrorCode.REQUEST_BODY_ERROR, Processing.getValidatedErrorList(bindingResult));
        }

        return authService.authLogin(userLoginVO,request);
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


    @GetMapping("/captcha")
    public BaseResponse captcha(HttpServletRequest request) {
        //产生验证码图片大小及长度130 宽  48 长 4 验证码长度
        GifCaptcha captcha=new GifCaptcha(130,48,4);
        //验证码的值
        String code=captcha.text();
        Map map =new HashMap<>();
        String key= UUID.randomUUID().toString().toLowerCase();
        log.info("\t\t>验证码被获取，验证码id为{}，验证码文本为{}，存在时间为{}秒",
                key,code,60);
        map.put("key",key);
        //img标签可以解码base64格式显示图片
        map.put("captchaImg",captcha.toBase64());
        captchaRedisUtil.setData(BusinessConstants.CAPTCHA,key,code,60);
        return ResultUtil.success("验证码",map);
    }


}


