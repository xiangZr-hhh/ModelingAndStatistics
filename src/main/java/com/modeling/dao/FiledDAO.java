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
import java.util.Map;

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


    /**
     * 检查目标列的类型与名称是否与数据表中某一列相同
     *
     * @param tableId 数据表id
     * @param filedName 列名称
     * @param filedType  列类型
     * @return boolean
     * @author zrx
     **/
    public boolean checkFiledTypeAndNameEquals (Long tableId,String filedName, String filedType) {

        List<Filed> filedList = this.getFiledListByTableId(tableId);

        boolean checkResult = true;
        for (Filed filed: filedList) {
            if (!filed.getName().equals(filedName) ||
                    !filed.getType().equals(filedType)) {
                checkResult = false;
            }

        }

        return checkResult;
    }


    /**
     * 删除数据表中的列 与要匹配的列 名称和类型不一样的数据
     * 如果该列不存在则创建
     *
     * @param tableId 数据表id
     * @param checkFileds 要匹配的列名称与类型数组
     * @return void
     * @author zrx
     **/
    public void updateNotEqualsFiled(Long tableId, Map<String,String> checkFileds){

//        获取数据表中的列数据
        List<Filed> filedList = this.getFiledListByTableId(tableId);
//        遍历，进行匹配检查
        for (Filed tableFiled : filedList){
            String fieldName = tableFiled.getName();
            String fieldType = tableFiled.getType();

            // 检查该列是否在要匹配的列中
            if (!checkFileds.containsKey(fieldName)) {
                // 如果不包含，则删除该列
                filedMapper.deleteById(tableFiled.getId());
            } else {
                // 如果包含，则检查类型是否匹配
                String expectedType = checkFileds.get(fieldName);
                if (!fieldType.equals(expectedType)) {
                    // 如果类型不匹配，则删除该列
                    filedMapper.deleteById(tableFiled.getId());
                }
            }
        }
//          遍历要匹配的列，如果该列不存在
        for (Map.Entry<String, String> entry : checkFileds.entrySet()) {
            String fieldName = entry.getKey();
            String fieldType = entry.getValue();

            boolean exists = false;
            for (Filed tableField : filedList) {
                if (tableField.getName().equals(fieldName) &&
                        tableField.getType().equals(fieldType)) {
                    exists = true;
                    break;
                }
            }

            if (!exists) {
                // 如果该列不存在，则创建它
                Filed filed = new Filed();
                filed.setType(fieldType)
                        .setName(fieldName)
                        .setTableId(tableId);
                filedMapper.insert(filed);
            }
        }

    }


}


