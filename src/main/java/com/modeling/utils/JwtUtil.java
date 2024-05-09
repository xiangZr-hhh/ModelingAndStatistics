package com.modeling.utils;

import com.modeling.common.SafeConstants;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;

import java.security.Key;
import java.util.regex.Pattern;

/**
 * @ClassName JwtUtil
 * @Description 验证与生成用户token
 * @Author zrx
 * @Date 2024/4/30 10:47
 */
@Slf4j
public class JwtUtil {

//    设置token过期时间为两天
    private static final long EXPIRATION_TIME = 172800000;


    /**
     *
     *
     * @param userId
     * @return java.lang.String
     * @author zrx
     * @description 生产用户token
     * @date 2024/4/30 10:52
     **/

    public static String generateToken(@NotNull Long userId) {
        Key key = Keys.hmacShaKeyFor(SafeConstants.getSecretKey().getBytes());
        return Jwts.builder()
                .setSubject(userId.toString())
                .setExpiration(new java.util.Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }


    /**
     *
     *
     * @param token
     * @return boolean
     * @author zrx
     * @description 用于验证token
     * @date 2024/4/30 10:51
     **/
    public static boolean verify(String token) {
        try {
            Long getTokenInUserId = getUserId(token);
            // 验证用户名是否匹配
            log.info("[FILTER] 令牌用户主键：{}", getTokenInUserId.toString());
            return Pattern.matches("^[0-9]+$", getTokenInUserId.toString());
        } catch (Exception e) {
            log.info("[FILTER] 令牌错误或失效");
            return false;
        }
    }


    /**
     *根据token获取用户id
     *
     * @param token token
     * @return java.lang.Long
     * @author zrx
     * @description 获取token中的用户id
     * @date 2024/4/30 10:52
     **/
    public static Long getUserId(String token) {
        Key key = Keys.hmacShaKeyFor(SafeConstants.getSecretKey().getBytes());
        Jws<Claims> claimsJws = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token);
        // 从JWT中获取用户名进行匹配
        long userId;
        try {
            userId = Long.parseLong(claimsJws.getBody().getSubject());
        } catch (NumberFormatException exception) {
            throw new NumberFormatException("用户ID格式错误");
        }
        return userId;
    }
}


