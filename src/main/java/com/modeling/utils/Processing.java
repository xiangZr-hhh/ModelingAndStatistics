package com.modeling.utils;


import com.modeling.exception.ClassCopyException;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Field;
import java.util.*;

/**
 * <h1>自定义快捷工具类</h1>
 * <hr/>
 *
 * @author 筱锋xiao_lfeng
 * @version v1.0.0
 * @since v1.0.0
 */
public class Processing {

    /**
     * <h1>获取参数校验错误信息</h1>
     * <hr/>
     * 用于获取参数校验错误信息
     *
     * @param bindingResult 参数校验结果
     * @return {@link ArrayList<String>}
     * @since v1.0.0
     */
    public static @NotNull ArrayList<String> getValidatedErrorList(BindingResult bindingResult) {
        ArrayList<String> arrayList = new ArrayList<>();
        for (ObjectError objectError : bindingResult.getAllErrors()) {
            arrayList.add(objectError.getDefaultMessage());
        }
        return arrayList;
    }


    /**
     * 将属性从源对象复制到目标对象。
     *
     * @param <T>    目标对象的类型。
     * @param <S>    源对象的类型。
     * @param source 从中复制属性的源对象。
     * @param target 属性将复制到的目标对象。
     * @throws ClassCopyException 如果在复制过程中出现错误。
     */
    @Contract(pure = true)
    public static <T, S> void copyProperties(@NotNull S source, @NotNull T target) throws ClassCopyException {
        Class<?> sourceClass = source.getClass();
        Class<?> targetClass = target.getClass();

        try {
            Field[] sourceFields = sourceClass.getDeclaredFields();
            for (Field sourceField : sourceFields) {
                String fieldName = sourceField.getName();
                Field targetField;
                try {
                    targetField = targetClass.getDeclaredField(fieldName);
                } catch (NoSuchFieldException e) {
                    // 目标对象不存在该属性，忽略
                    continue;
                }

                sourceField.setAccessible(true);
                targetField.setAccessible(true);

                Object value = sourceField.get(source);

                if (value == null) {
                    continue;
                }

                //如果获取的值不为数字且等于“”，则跳过
                if ("".equals(value)) {
                    continue;
                }

                if (!sourceField.getType().equals(targetField.getType())) {
                    continue;
                }

                targetField.set(target, value);
            }
        } catch (IllegalAccessException ignored) {
            throw new ClassCopyException();
        }
    }


    /**
     * <h2>获取Authorization Header</h2>
     * <hr/>
     * 用于获取Authorization Header
     *
     * @param request 请求
     */
    public static @Nullable Long getAuthHeaderToUserId(@NotNull HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        if (token == null || token.isEmpty()) {
            return null;
        } else {
            // 解析Bearer后面的令牌
            token = token.replace("Bearer ", "");
            return JwtUtil.getUserId(token);
        }
    }

    /**
     * <h1>生成256位字符串</h1>
     * <hr/>
     * 用于生成256位字符串（用于加密）
     *
     * @param input 输入
     * @return 返回加密后的字符串
     */
    public static String generateKey(Long input) {
        String inputString = String.valueOf(input);
        // 使用简单的伪随机算法生成256位字符串
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < 256; i++) {
            int charIndex;
            int math;
            if (i == 0) {
                math = Math.abs(inputString.hashCode());
            } else {
                math = Math.abs(inputString.hashCode() % i);
            }
            if (i < 62) {
                charIndex = (math % 62);
            } else {
                charIndex = (math / 6 % 62);
            }
            char nextChar = getCharFromIndex(charIndex);
            result.append(nextChar);
        }

        return result.toString();
    }


    /**
     * 根据数字位置，从26个字母里选择
     *
     * @param index  字串位置
     * @return char
     * @author zrx
     **/
    private static char getCharFromIndex(int index) {
        // 生成字符集合，可以根据需要自定义
        String charset = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        return charset.charAt(index);
    }

    // 从数据中提取归属地信息
    private String getPlaceFromData(String data) {
        // 实现根据数据提取归属地的逻辑，这里只是示例
        // 假设数据格式为 归属地:数据，例如："北京:123456"
        String[] parts = data.split(":");
        if (parts.length >= 2) {
            return parts[0];
        }
        return null;
    }


    /**
     * 按值排序map
     *
     * @param map
     * @return void
     * @author zrx
     **/
    public static Map<String, Integer> sortMapByValue(Map<String, Integer> map) {
        // 将原始 Map 的条目转换为 List
        List<Map.Entry<String, Integer>> entryList = new ArrayList<>(map.entrySet());

        // 按值从大到小排序
        Collections.sort(entryList, new Comparator<Map.Entry<String, Integer>>() {
            @Override
            public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) {
                return o2.getValue().compareTo(o1.getValue());
            }
        });

        // 使用 LinkedHashMap 保存排序后的结果
        Map<String, Integer> sortedMap = new LinkedHashMap<>();
        for (Map.Entry<String, Integer> entry : entryList) {
            sortedMap.put(entry.getKey(), entry.getValue());
        }

        return sortedMap;
    }
}
