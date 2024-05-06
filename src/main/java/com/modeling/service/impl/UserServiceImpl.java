package com.modeling.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.modeling.dao.UserDAO;
import com.modeling.mapper.UserMapper;
import com.modeling.model.entity.User;
import com.modeling.model.vodata.UserReturnVO;
import com.modeling.service.UserService;
import com.modeling.utils.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

/**
 * (User)表服务实现类
 *
 * @author zrx
 * @since 2024-04-30 20:37:54
 */
@Service("userService")
@RequiredArgsConstructor
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {


    private final UserDAO userDAO;

    private final UserMapper userMapper;


    /**
     * 根据token获取用户信息
     *
     * @param request  请求
     * @return com.modeling.utils.BaseResponse
     * @author zrx
     **/
    @Override
    public BaseResponse getUserByUsername( HttpServletRequest request) {

        Long uid = Processing.getAuthHeaderToUserId(request);

        User user = userMapper.selectById(uid);

        if (user == null) {
            return ResultUtil.error(ErrorCode.USER_NOT_EXIST);
        }

        UserReturnVO userReturnVO = new UserReturnVO();
        Processing.copyProperties(user,userReturnVO);
        userReturnVO.setRoleName(ConversionUtil.conversionRole(user.getRole()));

        return ResultUtil.success(userReturnVO);
    }



}
