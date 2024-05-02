package com.modeling.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.modeling.model.entity.DataTable;
import com.modeling.utils.BaseResponse;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;


/**
 * (DataTable)表服务接口
 *
 * @author zrx
 * @since 2024-04-30 15:48:31
 */
public interface DataTableService extends IService<DataTable> {

    BaseResponse createDataTable(MultipartFile[] files, String name, String remark, HttpServletRequest request);


    BaseResponse getTableDataById(Long id, HttpServletRequest request);

    BaseResponse getTable(HttpServletRequest request);

    BaseResponse getPageTableDataById(Long id,
                                      Integer page,
                                      Integer pageSize,
                                      HttpServletRequest request);
}

