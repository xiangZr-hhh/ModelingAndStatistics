package com.modeling.utils;

import com.modeling.model.entity.UpdatedLog;

import java.util.Arrays;
import java.util.List;

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
    public static List<String> conversionRole(Integer role){
        switch (role) {
            case 1:
                return Arrays.asList("admin","editor") ;
            case 2:
                return Arrays.asList("editor");
            default:
                return Arrays.asList("other");
        }
    }

    public static String conversionUpdatedLogColor(UpdatedLog updatedLog){
        switch (updatedLog.getType()) {
            case "feat":
                return "#0bbd87";
            case "fix":
                return "#E6A23C";
            case "patch":
                return "#909399";
            default:
                return "";
        }
    }


}


