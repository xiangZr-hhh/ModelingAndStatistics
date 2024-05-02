package com.modeling.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.modeling.model.entity.Filed;
import com.modeling.utils.BaseResponse;
import org.springframework.http.HttpRequest;

import javax.servlet.http.HttpServletRequest;
import java.util.List;


/**
 * (Filed)表服务接口
 *
 * @author zrx
 * @since 2024-04-30 20:37:52
 */
public interface FiledService extends IService<Filed> {

    BaseResponse getDataByFiledIdList(List<Long> filedIds, HttpServletRequest request);
}

