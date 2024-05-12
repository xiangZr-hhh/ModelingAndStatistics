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
     * 向指定数据表中增加Excel文件里的数据
     *
     * @param files Excel文件
     * @param id 数据表id
     * @param request 请求
     * @return com.modeling.utils.BaseResponse
     * @author zrx
     **/

    @PostMapping("increaseDataToTable")
    public BaseResponse increaseDataToTable (@RequestParam("files") MultipartFile[] files,
                                             @RequestParam("id") Long id,
                                             HttpServletRequest request) {

        if (id == null || files == null) {
            return ResultUtil.error(ErrorCode.PARAMETER_ERROR);
        }

        return dataTableService.increaseDataTable(files, id, request);
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



    /**
     * 分页获取指定id的数据表
     *
     * @param id 数据表id
     * @param page 当前页数
     * @param pageSize 每页大小
     * @param request  请求
     * @return com.modeling.utils.BaseResponse
     * @author zrx
     **/
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



    /**
     *  删除一个数据表
     *
     * @param id 数据表id
     * @param request  请求
     * @return com.modeling.utils.BaseResponse
     * @author zrx
     **/
   @DeleteMapping("deleteDataTable")
   public BaseResponse deleteDataTable(@RequestParam Long id,
                                        HttpServletRequest request) {

       if (id == null) {
           return ResultUtil.error(ErrorCode.PARAMETER_ERROR);
       }

       return dataTableService.deleteDataTable(id, request);
   }




   /**
    * 更新指定数据表的字段数据（会删除原有数据）
    *
    * @param id 数据表id
    * @param request  请求
    * @return com.modeling.utils.BaseResponse
    * @author zrx
    **/
   @PutMapping("updateDataTable")
   public BaseResponse updateDataTable(@RequestParam("files") MultipartFile[] files,
                                       @RequestParam("id") Long id,
                                       HttpServletRequest request) {

       if (id == null || files == null) {
           return ResultUtil.error(ErrorCode.PARAMETER_ERROR);
       }

       return dataTableService.updataDataTable(id,files, request);
   }


   /**
    * 获取系统统计数据
    *
    * @return com.modeling.utils.BaseResponse
    * @author zrx
    **/
   @GetMapping("getSystemTotal")
   public BaseResponse getAllDataTableNumber() {
       return dataTableService.getSystemTotal();
   }


   /**
    * 获取数据表信息
    *
    * @return com.modeling.utils.BaseResponse
    * @author zrx
    **/
   @GetMapping("getAllDataTableSimpleInfo")
    public BaseResponse getDataTableInfo() {
       return dataTableService.getAllDataTableInfo();
   }


}

