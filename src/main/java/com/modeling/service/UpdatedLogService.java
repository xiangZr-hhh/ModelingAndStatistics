package com.modeling.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.modeling.model.entity.UpdatedLog;
import com.modeling.model.vodata.UpdatedLogAddVO;
import com.modeling.utils.BaseResponse;
import com.sun.net.httpserver.HttpServer;
import org.springframework.http.HttpRequest;

import javax.servlet.http.HttpServletRequest;


/**
 * (UpdatedLog)表服务接口
 *
 * @author zrx
 * @since 2024-05-12 17:33:06
 */
public interface UpdatedLogService extends IService<UpdatedLog> {

	BaseResponse addUpdatedLog(UpdatedLogAddVO updatedLogAddVO, HttpServletRequest request);

	BaseResponse getAllUpdatedLog(HttpServletRequest request);
}

