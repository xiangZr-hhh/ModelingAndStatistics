package com.modeling.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.modeling.mapper.ModelingFileMapper;
import com.modeling.model.entity.ModelingFile;
import com.modeling.service.ModelingFileService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * (ModelingFile)表服务实现类
 *
 * @author zrx
 * @since 2024-04-30 20:37:53
 */
@Service("modelingFileService")
@RequiredArgsConstructor
public class ModelingFileServiceImpl extends ServiceImpl<ModelingFileMapper, ModelingFile> implements ModelingFileService {

}
