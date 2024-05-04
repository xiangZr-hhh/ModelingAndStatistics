package com.modeling.dao;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.modeling.mapper.FiledDataMapper;
import com.modeling.model.entity.Filed;
import com.modeling.model.entity.FiledData;
import com.modeling.model.vodata.FiledDataVO;
import com.modeling.utils.BaseResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 描述：处理字段数据的相关请求
 *
 * @author zrx
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class FiledDataDAO {


    private final FiledDataMapper filedDataMapper;

    private final LambdaQueryWrapper<FiledData> filedDataWrapper = new LambdaQueryWrapper<>();


    /**
     * 根据列id获取其数据
     *
     * @param filedId  列id
     * @return java.util.List<java.lang.String>
     * @author zrx
     **/
    public List<String> getFiledDataByFiledId (Long filedId) {

        filedDataWrapper.clear();
        filedDataWrapper.eq(FiledData::getFiledId,filedId);

        List<FiledData> fileData = filedDataMapper.selectList(filedDataWrapper);

        List<String> stringData = fileData.stream().map(FiledData::getData)
                .collect(Collectors.toList());

        return stringData;
    }



    /**
     * 分页获取列数据
     *
     * @param filedId  列id
     * @return java.util.List<java.lang.String>
     * @author zrx
     **/
    public List<String> getPageFiledDataByFiledId (Long filedId,
                                                    Integer page,
                                                   Integer pageSize) {
        // 创建分页对象
        Page<FiledData> pageQuery = new Page<>(page, pageSize);

        // 设置查询条件
        filedDataWrapper.clear();
        filedDataWrapper.eq(FiledData::getFiledId, filedId);

        // 执行分页查询
        IPage<FiledData> fileDataPage = filedDataMapper.selectPage(pageQuery, filedDataWrapper);

        //获取一列的数据
        List<FiledData> filedData = fileDataPage.getRecords();
        List<String> stringData = filedData.stream().map(FiledData::getData)
                .collect(Collectors.toList());

        return stringData;
    }


    /**
     * 根据列id删除对应数据
     *
     * @param id  列id
     * @return void
     * @author zrx
     **/
    public void deleteExcelDataByFiledId (Long id) {
        filedDataWrapper.clear();
        filedDataWrapper.eq(FiledData::getFiledId,id);
        filedDataMapper.delete(filedDataWrapper);
    }







}


