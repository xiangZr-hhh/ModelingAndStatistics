package com.modeling.config.filter;

import com.google.gson.Gson;

import com.modeling.utils.ErrorCode;
import com.modeling.utils.JwtUtil;
import com.modeling.utils.ResultUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.ExpiredCredentialsException;
import org.apache.shiro.web.filter.authc.BasicHttpAuthenticationFilter;
import org.springframework.stereotype.Component;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**

 *@ClassName JwtFilter

 *@Description Jwt过滤器

 *@Author 张睿相

 *@Date 2024-4-30 10:18:07

 */
@Slf4j
@Component
public class JwtFilter extends BasicHttpAuthenticationFilter {

    /**
     * 判断用户token是否合法
     *
     * @param request 请求
     * @param response 相应
     * @param mappedValue  映射值
     * @return boolean
     * @author zrx
     **/
    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
        // 判断是否存在Authorization Header
        String token = getAuthzHeader(request);
        if (token == null || token.isEmpty()) {
            return false; // 未提供Token，拒绝访问
        } else {
            // 解析Bearer后面的令牌
            token = token.replace("Bearer ", "");
            log.info("[FILTER] 请求令牌：" + token);
            return JwtUtil.verify(token);
        }
    }


    /**
     * 当访问被拒绝时，调用该方法
     *
     * @param request 请求
     * @param response 响应
     * @param mappedValue 映射值
     * @return boolean
     * @author zrx
     **/
    @Override
    protected boolean onAccessDenied(
            ServletRequest request,
            ServletResponse response,
            Object mappedValue) throws Exception {
        // 添加跨域禁止
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        CorsFilter.setHeader(httpServletResponse);
        // 程序执行
        try {
            // 尝试获取Authorization Header
            String token = getAuthzHeader(request);
            if (token == null || token.isEmpty()) {
                // 未提供Token，拒绝访问
                Gson gson = new Gson();
                response.setContentType("application/json;charset=UTF-8");
                response.getWriter().println(gson.toJson(ResultUtil.error(ErrorCode.UNAUTHORIZED)));
                return false;
            } else {
                // 解析Bearer后面的令牌
                token = token.replace("Bearer ", "");
                if (JwtUtil.verify(token)) {
                    // Token验证通过
                    return true;
                } else {
                    // Token验证失败，抛出异常
                    throw new ExpiredCredentialsException("Token已过期");
                }
            }
        } catch (ExpiredCredentialsException e) {
            // 处理Token过期异常，返回自定义的JSON信息
            Gson gson = new Gson();
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().println(gson.toJson(ResultUtil.error(ErrorCode.TOKEN_EXPIRED)));
            return false;
        }
    }


    /**
     *  从请求中获取Authorization里的token数据
     *
     * @param request 请求
     * @return java.lang.String
     * @author zrx
     **/
    @Override
    protected String getAuthzHeader(ServletRequest request) {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        return httpRequest.getHeader("Authorization");
    }
}
