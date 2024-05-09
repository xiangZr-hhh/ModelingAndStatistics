package com.modeling.controller;

import com.modeling.service.RecordService;
import com.modeling.utils.BaseResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 描述：操作记录控制层
 *
 * @author zrx
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("record")
public class RecordConreoller {

    private final RecordService recordService;

    /**
     * 获取本周活跃用户
     *
     * @return com.modeling.utils.BaseResponse
     * @author zrx
     **/
    @GetMapping("getWeekActiveUserNumber")
    public BaseResponse getWeekActiveUserNumber() {
        return recordService.getWeekActiveUserNumber();
    }



    /**
     * 获取所有操作记录
     *
     * @return com.modeling.utils.BaseResponse
     * @author zrx
     **/
    @GetMapping("getAllRecord")
    public BaseResponse getAllRecord() {
        return recordService.getAllRecord();
    }


}


