package com.modeling.dao;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.modeling.mapper.DataTableMapper;
import com.modeling.model.entity.DataTable;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 描述：用于处理数据表相关的请求
 *
 * @author zrx
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class DataTableDAO {

    private final DataTableMapper dataTableMapper;

    private final LambdaQueryWrapper<DataTable> dataTableWrapper = new LambdaQueryWrapper<>();


    public boolean isExistDataTableByName (String name) {
        dataTableWrapper.clear();
        dataTableWrapper.eq(DataTable::getName,name);

        List<DataTable> dataTables = dataTableMapper.selectList(dataTableWrapper);

        return dataTables.size() > 0;
    }

    public boolean isExistDataTableById (Long id) {
        dataTableWrapper.clear();

        DataTable dataTable = dataTableMapper.selectById(id);

        if (dataTable == null) {
            return false;
        }

        return true;
    }

}


