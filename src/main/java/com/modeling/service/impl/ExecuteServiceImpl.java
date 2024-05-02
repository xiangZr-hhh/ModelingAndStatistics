package com.modeling.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.modeling.mapper.ExecuteMapper;
import com.modeling.model.entity.Execute;
import com.modeling.service.ExecuteService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * (Execute)表服务实现类
 *
 * @author zrx
 * @since 2024-04-30 22:44:10
 */
@Service("executeService")
@RequiredArgsConstructor
public class ExecuteServiceImpl extends ServiceImpl<ExecuteMapper, Execute> implements ExecuteService {

}
