package com.modeling.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.modeling.model.entity.Record;
import com.modeling.utils.BaseResponse;


/**
 * (Record)表服务接口
 *
 * @author zrx
 * @since 2024-05-02 17:56:16
 */
public interface RecordService extends IService<Record> {

    BaseResponse getWeekActiveUserNumber();

    BaseResponse getAllRecord();
}

