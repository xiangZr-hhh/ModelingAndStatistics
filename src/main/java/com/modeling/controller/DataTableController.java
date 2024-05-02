package com.modeling.controller;

import com.modeling.service.DataTableService;
import com.modeling.utils.BaseResponse;
import com.modeling.utils.ErrorCode;
import com.modeling.utils.ResultUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.regex.Pattern;


/**
 * (DataTable)表控制层
 *
 * @author zrx
 * @since 2024-04-30 15:48:29
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("excelData")
public class DataTableController  {

    private final DataTableService dataTableService;

    /**
     * 根据Excel文件添加数据文件
     *
     * @param files Excel文件
     * @param name 文件名称
     * @param remark 备注
     * @param request  请求
     * @return com.modeling.utils.BaseResponse
     * @author zrx
     **/
    @PostMapping("addDataTable")
    public BaseResponse checkExcelHeader (@RequestParam("files") MultipartFile[] files,
                                          @RequestParam("name") String name,
                                          String remark,
                                          HttpServletRequest request) {

        return dataTableService.createDataTable(files,name,remark, request);
    }


    /**
     *  获取数据表内全部信息
     *
     * @param id 数据表id
     * @param request  请求
     * @return com.modeling.utils.BaseResponse
     * @author zrx
     **/
    @GetMapping("getAllDataTableById")
    public BaseResponse getDataTableById (@RequestParam Long id,
                                          HttpServletRequest request) {

        if (id == null) {
            return ResultUtil.error(ErrorCode.PARAMETER_ERROR);
        }

        return dataTableService.getTableDataById(id, request);
    }



    @GetMapping("getPageDataTableById")
    public BaseResponse getPageDataTableById (@RequestParam Long id,
                                              @RequestParam Integer page,
                                              @RequestParam Integer pageSize,
                                              HttpServletRequest request) {
        if (id == null) {
            return ResultUtil.error(ErrorCode.PARAMETER_ERROR);
        }

        // 定义分页参数校验的正则表达式
        String pageSizeRegex = "^([1-9]\\d{0,2}|3000)$"; // pageSize小于等于3000
        String pageRegex = "^\\d+$"; // page为非负整数

        // 校验分页参数
        if (!Pattern.matches(pageSizeRegex, String.valueOf(pageSize))) {
            return ResultUtil.error(ErrorCode.PARAMETER_ERROR, "pageSize过大或格式错误");
        }
        if (!Pattern.matches(pageRegex, String.valueOf(page))) {
            return ResultUtil.error(ErrorCode.PARAMETER_ERROR, "page参数过小或格式错误");
        }

        return dataTableService.getPageTableDataById(id, page, pageSize,request);
    }


    /**
     * 获取所有数据表信息
     *
     * @param request 请求
     * @return com.modeling.utils.BaseResponse
     * @author zrx
     **/
    @GetMapping("getAllTable")
    public BaseResponse getAllTable(HttpServletRequest request) {
        return dataTableService.getTable(request);
    }






}

