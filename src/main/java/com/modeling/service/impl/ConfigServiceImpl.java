package com.modeling.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.modeling.mapper.ConfigMapper;
import com.modeling.model.entity.Config;
import com.modeling.service.ConfigService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * (Config)表服务实现类
 *
 * @author zrx
 * @since 2024-04-30 20:37:52
 */
@Service("configService")
@RequiredArgsConstructor
public class ConfigServiceImpl extends ServiceImpl<ConfigMapper, Config> implements ConfigService {

}
