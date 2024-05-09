package com.modeling.service.impl;

import com.modeling.common.BusinessConstants;
import com.modeling.dao.UserDAO;
import com.modeling.mapper.RecordMapper;
import com.modeling.mapper.UserMapper;
import com.modeling.model.entity.Record;
import com.modeling.model.entity.User;
import com.modeling.model.vodata.UserLoginVO;
import com.modeling.model.vodata.UserRegisterVO;
import com.modeling.model.vodata.UserReturnVO;
import com.modeling.service.AuthService;
import com.modeling.utils.*;
import com.modeling.utils.redis.CaptchaRedisUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * 描述：用户认证控制层
 *
 * @author zrx
 */
@Service("authService")
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {

    private final UserDAO userDAO;
    private final RecordMapper recordMapper;
    private final UserMapper userMapper;
    private final CaptchaRedisUtil captchaRedisUtil;


    /**
     * 用户注册
     *
     * @param userRegisterVO  用户注册信息的值对象
     * @return com.modeling.utils.BaseResponse
     * @author zrx
     **/
    @Override
    public BaseResponse authRegister(UserRegisterVO userRegisterVO) {

        String username = userRegisterVO.getUsername();
        String password = userRegisterVO.getPassword();

        // 检查用户是否存在
        if (userDAO.checkUserIsExistByUsername(username)) {
            return ResultUtil.error(ErrorCode.USER_EXIST);
        }

        //加密密码
        String encryptedPassword = BCrypt.hashpw(password, BCrypt.gensalt());

        //设置属性
        User user = new User();
        user.setUsername(username)
                .setPassword(encryptedPassword);
        //向数据库添加数据
        userMapper.insert(user);

        return ResultUtil.success("用户注册成功");
    }


    /**
     * 用户退出成功
     *
     * @param request  请求
     * @return com.modeling.utils.BaseResponse
     * @author zrx
     **/
    @Override
    public BaseResponse authLogout(HttpServletRequest request) {
        return ResultUtil.success("退出成功");
    }


    /**
     * 用户登录
     *
     * @param userLoginVO 用户登录信息的值对象
     * @return com.modeling.utils.BaseResponse
     * @author zrx
     **/
    @Override
    public BaseResponse authLogin(UserLoginVO userLoginVO,HttpServletRequest request) {

//        获取数据
        String username = userLoginVO.getUsername();
        String password = userLoginVO.getPassword();
        String captchaCode = userLoginVO.getCaptchaCode();
        String captchaId = userLoginVO.getCaptchaId();

//        校验验证码
        String captachaText = (String) captchaRedisUtil.getData(
                BusinessConstants.CAPTCHA,captchaId);
        System.out.println(captchaId);
        System.out.println(captchaCode);
        System.out.println(captachaText);
        if (captachaText == null || captachaText.isEmpty() || !captachaText.equalsIgnoreCase(captchaCode)) {
            return ResultUtil.error(ErrorCode.CAP_CHECK_ERROR);
        }

//        根据用户名获取用户
        User user = userDAO.getUserByUsername(username);
//        如果为空，返回用户名错误
        if (user == null) {
            return ResultUtil.error(ErrorCode.USER_NOT_EXIST);
        }

//        添加记录
        Record record = new Record();
        record.setType("登录")
                .setContent("用户"+userDAO.getUserNameById(user.getId())+"登录")
                .setIp(IpUtils.getIpAddr(request))
                .setUserId(user.getId());
        recordMapper.insert(record);


//        校验密码
        if (BCrypt.checkpw(password, user.getPassword())) {
            log.info("\t\t> 登录成功，用户 [{}]{}", user.getId(), user.getUsername());
            return ResultUtil.success(this.encapsulateDisplayContent(user));
        } else {
            return ResultUtil.error(ErrorCode.WRONG_PASSWORD);
        }

    }



    /**
     *  封装用户结果实体类
     *
     * @param user
     * @return com.modeling.model.vodata.UserReturnVO
     * @author zrx
     **/
    private UserReturnVO encapsulateDisplayContent(User user) {
        // 授权 Token
        String token = JwtUtil.generateToken(user.getId());
        // 新建用户封装实体类
        UserReturnVO userReturnVO = new UserReturnVO();
        // 先复制相同的属性
        Processing.copyProperties(user, userReturnVO);
        // 复制其他需要转换或查询的数据
        userReturnVO.setToken(token);
        userReturnVO.setRoleName(ConversionUtil.conversionRole(user.getRole()));
        return userReturnVO;
    }

    

}


