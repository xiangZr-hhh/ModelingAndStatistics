package com.modeling.controller;

import com.alibaba.fastjson.JSONObject;
import com.google.code.kaptcha.Producer;
import com.modeling.model.vodata.UserLoginVO;
import com.modeling.model.vodata.UserRegisterVO;
import com.modeling.service.AuthService;
import com.modeling.utils.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Base64Utils;
import org.springframework.util.FastByteArrayOutputStream;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotNull;
import java.awt.image.BufferedImage;
import java.io.IOException;

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


    /**
     * 生成验证码图片
     *
     * @return com.modeling.utils.BaseResponse
     * @author zrx
     **/
    @GetMapping("/captchaImage")
    public BaseResponse captchaImage() {
//        创建验证码
        String capStr = null;
        BufferedImage image = null;
//        生成验证
        String capText = CaptchaCreateUtil.getKaptchaBeanMath().createText();
        capStr = capText.substring(0, capText.lastIndexOf("@"));
        image = CaptchaCreateUtil.getKaptchaBeanMath().createImage(capStr);
//        转换为流对象写出
        FastByteArrayOutputStream os = new FastByteArrayOutputStream();
        try {
            ImageIO.write(image, "jpg", os);
        } catch (IOException e) {
            return ResultUtil.error(ErrorCode.CAP_CREATE_ERROR);
        }
//        生成验证码图片json
        JSONObject result = new JSONObject();
        result.put("img", Base64Utils.encode(os.toByteArray()));

        return ResultUtil.success(result);
    }




}


