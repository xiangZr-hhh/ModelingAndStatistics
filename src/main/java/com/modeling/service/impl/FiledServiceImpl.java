package com.modeling.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.modeling.mapper.FiledMapper;
import com.modeling.model.entity.Filed;
import com.modeling.service.FiledService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * (Filed)表服务实现类
 *
 * @author zrx
 * @since 2024-04-30 20:37:52
 */
@Service("filedService")
@RequiredArgsConstructor
public class FiledServiceImpl extends ServiceImpl<FiledMapper, Filed> implements FiledService {

}
