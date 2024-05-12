package com.modeling.dao;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.modeling.mapper.DataTableMapper;
import com.modeling.model.entity.DataTable;
import com.modeling.model.entity.Filed;
import com.modeling.service.DataTableService;
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
    private final FiledDAO filedDAO;

    private final LambdaQueryWrapper<DataTable> dataTableWrapper = new LambdaQueryWrapper<>();


    /**
     * 根据数据表名称获取数据
     *
     * @param name 数据表名称
     * @return boolean
     * @author zrx
     **/
    public boolean isExistDataTableByName (String name) {
        dataTableWrapper.clear();
        dataTableWrapper.eq(DataTable::getName,name);

        List<DataTable> dataTables = dataTableMapper.selectList(dataTableWrapper);

        return dataTables.size() > 0;
    }

    /**
     * 根据id检测数据表是否存在
     *
     * @param id 数据表id
     * @return boolean
     * @author zrx
     **/
    public boolean isExistDataTableById (Long id) {
        dataTableWrapper.clear();

        DataTable dataTable = dataTableMapper.selectById(id);

        if (dataTable == null) {
            return false;
        }

        return true;
    }

    /**
     * 获取数据表数量
     *
     * @return java.lang.Integer
     * @author zrx
     **/
    public Integer getAllDataTableNumber() {
        List<DataTable> dataTables = dataTableMapper.selectList(null);
        return dataTables.size();
    }


    /**
     * 获取所有数据表的数据总量之和
     *
     * @return java.lang.Integer
     * @author zrx
     **/
    public Integer getExcelDataTotal() {
//        获取所有数据表
        List<DataTable> dataTables = dataTableMapper.selectList(null);
//        定义数据量
        Integer dataSize = 0;
//        遍历数据表和列，获取所有的列数据量并相加
        for(DataTable dataTable: dataTables) {
            List<Filed> filedList = filedDAO.
                    getFiledListByTableId(dataTable.getId());
            for (Filed filed: filedList) {
                dataSize += filed.getFiledDataSize();
            }
        }

        return dataSize;
    }



}


