package com.modeling.utils;

/**
 * 描述：用于实体类封装时的数据转换
 *
 * @author zrx
 */
public class ConversionUtil {


    /**
     * 转换翻译转换用户权限
     *
     * @param role
     * @return java.lang.String
     * @author zrx
     **/
    public static String conversionRole(Integer role){
        switch (role) {
            case 1:
                return "admin";
            case 2:
                return "editor";
            default:
                return "other";
        }
    }


}


