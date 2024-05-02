package com.modeling.dao;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.modeling.mapper.DataTableMapper;
import com.modeling.mapper.FiledMapper;
import com.modeling.model.entity.Filed;
import com.sun.org.apache.xml.internal.dtm.DTM;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 描述：用于处理Filed相关请求
 *
 * @author zrx
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class FiledDAO {

    private final FiledMapper filedMapper;
    private final DataTableMapper dataTableMapper;

    private final LambdaQueryWrapper<Filed> filedWrapper = new LambdaQueryWrapper<>();


    /**
     * 根据数据表id获取所有字段
     *
     * @param tableId  数据表id
     * @return java.util.List<com.modeling.model.entity.Filed>
     * @author zrx
     **/
    public List<Filed> getFiledListByTableId (Long tableId) {
        filedWrapper.clear();
        filedWrapper.eq(Filed::getTableId,tableId);
        List<Filed> filedList = filedMapper.selectList(filedWrapper);

        return filedList;
    }


}


