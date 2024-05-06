package com.modeling.controller;

import com.modeling.service.UserService;
import com.modeling.utils.BaseResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;


/**
 * (User)表控制层
 *
 * @author zrx
 * @since 2024-04-30 20:37:53
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("user")
public class UserController {

    private final UserService userService;

    @GetMapping("getMyUserInformation")
    public BaseResponse getUserByUsername(HttpServletRequest request) {
        return userService.getUserByUsername(request);
    }


}

