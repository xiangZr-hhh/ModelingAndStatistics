package com.modeling.controller;

import com.modeling.model.vodata.UpdatedLogAddVO;
import com.modeling.service.RecordService;
import com.modeling.service.UpdatedLogService;
import com.modeling.utils.BaseResponse;
import com.modeling.utils.ErrorCode;
import com.modeling.utils.Processing;
import com.modeling.utils.ResultUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpRequest;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

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

    private final UpdatedLogService updatedLogService;

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


    /**
     * 添加更新日志
     *
     * @param updatedLogAddVO
     * @param bindingResult
     * @param request
     * @return com.modeling.utils.BaseResponse
     * @author zrx
     **/
    @PostMapping("addUpdatedLog")
    public BaseResponse addUpdatedLog(@RequestBody @Validated UpdatedLogAddVO updatedLogAddVO,
                                      @NotNull BindingResult bindingResult,
                                      HttpServletRequest request) {

        if (bindingResult.hasErrors()) {
            return ResultUtil.error(ErrorCode.REQUEST_BODY_ERROR,
                    Processing.getValidatedErrorList(bindingResult));
        }

        return updatedLogService.addUpdatedLog(updatedLogAddVO,request);
    }

}


