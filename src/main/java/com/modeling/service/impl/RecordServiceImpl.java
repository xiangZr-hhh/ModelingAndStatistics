package com.modeling.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.modeling.mapper.RecordMapper;
import com.modeling.model.entity.Record;
import com.modeling.service.RecordService;
import org.springframework.stereotype.Service;

/**
 * (Record)表服务实现类
 *
 * @author zrx
 * @since 2024-05-02 17:56:16
 */
@Service("recordService")
public class RecordServiceImpl extends ServiceImpl<RecordMapper, Record> implements RecordService {

}
