package com.modeling.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.modeling.mapper.FiledDataMapper;
import com.modeling.model.entity.FiledData;
import com.modeling.service.FiledDataService;
import org.springframework.stereotype.Service;

/**
 * (FiledData)表服务实现类
 *
 * @author zrx
 * @since 2024-04-30 20:45:16
 */
@Service("filedDataService")
public class FiledDataServiceImpl extends ServiceImpl<FiledDataMapper, FiledData> implements FiledDataService {

}
