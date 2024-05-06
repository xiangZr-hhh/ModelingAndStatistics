package com.modeling.controller;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.modeling.utils.BaseResponse;
import com.modeling.utils.ResultUtil;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * 描述：前端面板数据层
 *
 * @author zrx
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("test")
public class MenuController {


    @GetMapping("transaction/list1")
    public BaseResponse getTransactionList(HttpServletRequest request){
        return ResultUtil.success(generateTransactionData());
    }


    @GetMapping("route")
    public BaseResponse getRoute(HttpServletRequest request) {
        // 你提供的 JavaScript 对象
        String jsonString = "[ { \"path\": \"/permission\", \"component\": \"layout/Layout\", \"redirect\": \"/permission/index\", \"alwaysShow\": true, \"meta\": { \"title\": \"permission\", \"icon\": \"lock\", \"roles\": [ \"admin\", \"editor\" ] }, \"children\": [ { \"path\": \"page\", \"component\": \"views/permission/page\", \"name\": \"PagePermission\", \"meta\": { \"title\": \"pagePermission\", \"roles\": [ \"admin\" ] } }, { \"path\": \"directive\", \"component\": \"views/permission/directive\", \"name\": \"DirectivePermission\", \"meta\": { \"title\": \"directivePermission\" } }, { \"path\": \"role\", \"component\": \"views/permission/role\", \"name\": \"RolePermission\", \"meta\": { \"title\": \"rolePermission\", \"roles\": [ \"admin\" ] } } ] }, { \"path\": \"/icon\", \"component\": \"layout/Layout\", \"children\": [ { \"path\": \"index\", \"component\": \"views/icons/index\", \"name\": \"Icons\", \"meta\": { \"title\": \"icons\", \"icon\": \"icon\", \"noCache\": true } } ] } /* Add other routes here */ ]";

        // 使用 Fastjson 转换为 JSON 字符串

        return ResultUtil.success(JSON.parse(jsonString));
    }


    // 定义返回的数据结构
    public static class ApiResponse {
        private int code;
        private Object data;

        public ApiResponse(int code, Object data) {
            this.code = code;
            this.data = data;
        }

        // Getters and setters
    }



    // 生成交易数据的方法，这里是示例数据，您可以根据实际情况生成真实数据
    private TransactionData generateTransactionData() {
        TransactionData data = new TransactionData();
        data.setTotal(20); // 假设总数为20

        List<TransactionItem> items = new ArrayList<>();
        Random random = new Random();

        for (int i = 0; i < 20; i++) {
            TransactionItem item = new TransactionItem();
            item.setOrderNo(UUID.randomUUID().toString());
            item.setTimestamp(new Date());
            item.setUsername("User " + i);
            item.setPrice(random.nextDouble() * (15000 - 1000) + 1000);
            item.setStatus(random.nextBoolean() ? "success" : "pending");
            items.add(item);
        }

        data.setItems(items);
        return data;
    }

    // 定义交易数据结构
    @Data
    public static class TransactionData {
        private int total;
        private List<TransactionItem> items;

        // Getters and setters
    }

    // 定义交易项数据结构
    @Data
    public static class TransactionItem {
        private String orderNo;
        @JsonFormat
        private Date timestamp;
        private String username;
        private double price;
        private String status;

        // Getters and setters
    }


}


