package com.modeling.controller;


import com.modeling.service.FiledService;
import com.modeling.utils.BaseResponse;
import com.modeling.utils.ErrorCode;
import com.modeling.utils.ResultUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpRequest;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * (Filed)表控制层
 *
 * @author zrx
 * @since 2024-04-30 20:37:52
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("filed")
public class FiledController  {


    private final FiledService filedService;


    /**
     * 根据列id获取对应数据
     *
     * @param filedIds 列id
     * @param request  请求
     * @return com.modeling.utils.BaseResponse
     * @author zrx
     **/
    @GetMapping("getDataByFiledIdList")
    public BaseResponse getDataByFiledIdList(@RequestParam List<Long> filedIds,
            HttpServletRequest request) {

//        检测filedIds是否为空
        if (filedIds.isEmpty()) {
            return ResultUtil.error(ErrorCode.PARAMETER_ERROR);
        }
        for (Long id : filedIds) {
            if (id == null) {
                return ResultUtil.error(ErrorCode.PARAMETER_ERROR);
            }
        }

        return filedService.getDataByFiledIdList(filedIds, request);
    }

}

