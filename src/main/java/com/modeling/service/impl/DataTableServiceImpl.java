package com.modeling.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.modeling.common.ExecuteActionTypeConstant;
import com.modeling.dao.DataTableDAO;
import com.modeling.dao.FiledDAO;
import com.modeling.dao.FiledDataDAO;
import com.modeling.dao.UserDAO;
import com.modeling.mapper.*;
import com.modeling.model.entity.*;
import com.modeling.model.vodata.DataTableVO;
import com.modeling.model.vodata.FiledDataVO;
import com.modeling.service.DataTableService;
import com.modeling.utils.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;


/**
 * (DataTable)表服务实现类
 *
 * @author zrx
 * @since 2024-04-30 15:48:31
 */
@Slf4j
@Service("dataTableService")
@RequiredArgsConstructor
public class DataTableServiceImpl extends ServiceImpl<DataTableMapper, DataTable> implements DataTableService {

    private final DataTableDAO dataTableDAO;
    private final FiledDAO filedDAO;
    private final FiledDataDAO filedDataDAO;
    private final UserDAO userDAO;

    private final DataTableMapper dataTableMapper;
    private final FiledMapper filedMapper;
    private final ExecuteMapper executeMapper;
    private final FiledDataMapper filedDataMapper;
    private final RecordMapper recordMapper;



    /**
     * 创建Excel文件的数据表
     *
     * @param files Excel文件
     * @param name 数据表名称
     * @param remark 备注
     * @param request 请求
     * @return com.modeling.utils.BaseResponse
     * @author zrx
     **/
    @Override
    public BaseResponse createDataTable(MultipartFile[] files,
                                        String name,
                                        String remark,
                                        HttpServletRequest request) {

        log.info("\t\t> 服务器成功接受Excel文件，开始解析");

//        从请求头中获取用户id
        Long uid = Processing.getAuthHeaderToUserId(request);

//        检测Excel数据表是否重名
        if (dataTableDAO.isExistDataTableByName(name)) {
            return ResultUtil.error(ErrorCode.DATA_TABLE_EXIST);
        }
//        检验Excel文件的列名
        BaseResponse headerCheckResult = ExcelReadUtil.checkExcelSheetHeader(files,null);
        if (headerCheckResult.getCode() != 200) {
            return headerCheckResult;
        }

//        根据表头,设定数据表实体类的表头字段数据总数
        List<String> sheetHeader = (List<String>) headerCheckResult.getData();

//        检验Excel文件的数据格式
        BaseResponse dataCheckResult = ExcelReadUtil.checkExcelSheetData(files, sheetHeader,null);
        if (dataCheckResult.getCode() != 200) {
            return dataCheckResult;
        }

//        获取sheet表头字段的类型
        List<String> sheetHeaderType = (List<String>) dataCheckResult.getData();

//        获取Excel表数据
        List<List<String>> excelData = ExcelReadUtil.getAllExcelDataByHeaderName(files,
                sheetHeader);

//        创建对应Excel数据表实体类
        DataTable dataTable = new DataTable();
        dataTable.setName(name);
        if (remark != null){
            dataTable.setRemark(remark);
        }
        dataTable.setCreatedBy(Processing.getAuthHeaderToUserId(request));
        dataTable.setFiledTotal(sheetHeader.size());
        dataTable.setDataTotal(excelData.size()*sheetHeader.size());
//        向数据库插入数据
        dataTableMapper.insert(dataTable);

//        定义储存创建的字段实体类的数字
        List<Filed> newFiledList = new ArrayList<>();

//        根据表头创建字段属性,并添加表头下的数据
        for (int k = 0;k < sheetHeader.size(); k++) {
            String filedName = sheetHeader.get(k);

            Filed sheetFiled = new Filed();
            sheetFiled.setName(filedName)
                    .setTableId(dataTable.getId())
                    .setType(sheetHeaderType.get(k))
                    .setFiledDataSize(excelData.size());
//            向数据库插入数据
            filedMapper.insert(sheetFiled);
            newFiledList.add(sheetFiled);
        }

//        临时储存Excel数据，用户批量插入
        List<FiledData> filedDataList = new ArrayList<>();
//        根据列名添加数据
        for (int i = 0;i < newFiledList.size();i++) {
            Filed filed = newFiledList.get(i);
            //        创建一条操作行为实体类
            Execute execute = new Execute();
            execute.setCreatedBy(Processing.getAuthHeaderToUserId(request))
                    .setTableId(dataTable.getId())
                    .setFileId(filed.getId())
                    .setType(ExecuteActionTypeConstant.ADD);
            //        向数据库添加数据
            executeMapper.insert(execute);

            //        正向列遍历Excel数据
            for (int rnb = 0; rnb < excelData.size(); rnb++ ){

                List<String> rowData = excelData.get(rnb);

                String cellData = rowData.get(i);

                FiledData filedData = new FiledData();
                filedData.setData(cellData)
                        .setExecuteId(execute.getId())
                        .setCreatedTime(new Date())
                        .setIsDelete(0)
                        .setFiledId(filed.getId());
                filedDataList.add(filedData);

                if (filedDataList.size() >= 1000){
                    log.info("\t\t> 正在向Excel数据表【{}】插入了1000条数据,共计{}条",dataTable.getName(),(i+1)*(rnb+1) );
                    filedDataMapper.insertBatchSomeColumn(filedDataList);
                    filedDataList.clear();
                }
            }

        }

        if (!filedDataList.isEmpty()) {
            filedDataMapper.insertBatchSomeColumn(filedDataList);
            log.info("\t\t> 正在向Excel数据表【{}】插入了{}条数据",dataTable.getName(),filedDataList.size());
        }

        Record record = new Record();
        record.setType("创建")
                .setContent("用户"+userDAO.getUserNameById(uid)+"创建了数据表" +
                        dataTable.getName())
                .setIp(IpUtils.getIpAddr(request))
                .setUserId(uid);
        recordMapper.insert(record);

        return ResultUtil.success();
    }


    /**
     * 数据表添加Excel里的文件数据
     *
     * @param files Excel文件
     * @param id 数据表id
     * @param request 请求
     * @return com.modeling.utils.BaseResponse
     * @author zrx
     **/
    @Override
    public BaseResponse increaseDataTable(MultipartFile[] files, Long id, HttpServletRequest request) {

        log.info("\t\t> 服务器成功接受Excel文件，开始添加数据");

//        从请求头中获取用户id
        Long uid = Processing.getAuthHeaderToUserId(request);

//        获取对应数据表
        if (!dataTableDAO.isExistDataTableById(id)) {
            return ResultUtil.error(ErrorCode.EXCEL_DATA_NULL);
        }
        DataTable dataTable = dataTableMapper.selectById(id);

//        获取数据表字段与表头
        List<Filed> filedList = filedDAO.getFiledListByTableId(dataTable.getId());
        List<String> headerName = filedList.stream()
                .map(filed -> filed.getName()).collect(Collectors.toList());

//        检验Excel文件的列名
        BaseResponse headerCheckResult = ExcelReadUtil.checkExcelSheetHeader(files,headerName);
        if (headerCheckResult.getCode() != 200) {
            return headerCheckResult;
        }
//        检验Excel文件的数据格式
        BaseResponse dataCheckResult = ExcelReadUtil.checkExcelSheetData(files, headerName,filedList);
        if (dataCheckResult.getCode() != 200) {
            return dataCheckResult;
        }

//        获取Excel表数据
        List<List<String>> excelData = ExcelReadUtil.getAllExcelDataByHeaderName(files,
                headerName);

//        更新数据表的数据总数
        dataTable.setDataTotal(dataTable.getDataTotal()+
                (excelData.size()*filedList.size()));
        dataTableMapper.updateById(dataTable);

//        临时储存Excel数据，用户批量插入
        List<FiledData> filedDataList = new ArrayList<>();
//        根据列名添加数据
        for (int i = 0;i < filedList.size();i++) {
//            更新列数据
            Filed filed = filedList.get(i);
            filed.setFiledDataSize(filed.getFiledDataSize()+
                    excelData.size());
            filedMapper.updateById(filed);
            //        创建一条操作行为实体类
            Execute execute = new Execute();
            execute.setCreatedBy(Processing.getAuthHeaderToUserId(request))
                    .setTableId(dataTable.getId())
                    .setFileId(filed.getId())
                    .setType(ExecuteActionTypeConstant.INCREASE);
            //        向数据库添加数据
            executeMapper.insert(execute);

            //        正向列遍历Excel数据
            for (int rnb = 0; rnb < excelData.size(); rnb++ ){

                List<String> rowData = excelData.get(rnb);

                String cellData = rowData.get(i);

                FiledData filedData = new FiledData();
                filedData.setData(cellData)
                        .setExecuteId(execute.getId())
                        .setFiledId(filed.getId())
                        .setCreatedTime(new Date())
                        .setIsDelete(0);
                filedDataList.add(filedData);

                if (filedDataList.size() >= 1000){
                    log.info("\t\t> 正在向Excel数据表【{}】插入了1000条数据,共计{}条",dataTable.getName(),(i+1)*(rnb+1) );
                    filedDataMapper.insertBatchSomeColumn(filedDataList);
                    filedDataList.clear();
                }
            }

        }

        if (!filedDataList.isEmpty()) {
            filedDataMapper.insertBatchSomeColumn(filedDataList);
            log.info("\t\t> 正在向Excel数据表【{}】插入了{}条数据",dataTable.getName(),filedDataList.size());
        }

        Record record = new Record();
        record.setType("添加")
                .setContent("用户"+userDAO.getUserNameById(uid)+"向数据表" +
                        dataTable.getName()+"添加了数据")
                .setIp(IpUtils.getIpAddr(request))
                .setUserId(uid);
        recordMapper.insert(record);

        return ResultUtil.success();
    }


    /**
     * 根据Excel文件更新指定数据表
     *
     * 会保留列名和列类型相同的字段
     *
     * @param tableId 数据表id
     * @param request  请求
     * @return com.modeling.utils.BaseResponse
     * @author zrx
     **/
    @Override
    public BaseResponse updataDataTable(Long tableId,MultipartFile[] files, HttpServletRequest request) {

        log.info("\t\t> 服务器成功接受Excel文件，开始更新数据");

//        从请求头中获取用户id
        Long uid = Processing.getAuthHeaderToUserId(request);

//        获取对应数据表
        if (!dataTableDAO.isExistDataTableById(tableId)) {
            return ResultUtil.error(ErrorCode.EXCEL_DATA_NULL);
        }
        DataTable dataTable = dataTableMapper.selectById(tableId);

//        检验Excel文件的列名
        BaseResponse headerCheckResult = ExcelReadUtil.checkExcelSheetHeader(files,null);
        if (headerCheckResult.getCode() != 200) {
            return headerCheckResult;
        }

//        获取sheet表头字段的名称
        List<String> sheetHeader = (List<String>) headerCheckResult.getData();

//        检验Excel文件的数据格式
        BaseResponse dataCheckResult = ExcelReadUtil.checkExcelSheetData(files, sheetHeader,null);
        if (dataCheckResult.getCode() != 200) {
            return dataCheckResult;
        }

//        获取sheet表头字段的类型
        List<String> sheetHeaderType = (List<String>) dataCheckResult.getData();
//        将类型与类型匹配为Map键值对
        Map<String, String> checkFields = new HashMap<>();
        for (int i = 0; i < sheetHeader.size(); i++) {
            String fieldName = sheetHeader.get(i);
            String fieldType = sheetHeaderType.get(i);
            checkFields.put(fieldName, fieldType);
        }

//        更新数据表字段(删除不匹配的字段，并填加新的字段)
        filedDAO.updateNotEqualsFiled(tableId, checkFields);


//        获取Excel表数据
        List<List<String>> excelData = ExcelReadUtil.getAllExcelDataByHeaderName(files,
                sheetHeader);

//        获取更新后的列数据
        List<Filed> filedList = filedDAO.getFiledListByTableId(tableId);

//        临时储存Excel数据，用户批量插入
        List<FiledData> filedDataList = new ArrayList<>();
//        根据列名添加数据
        for (int i = 0;i < filedList.size();i++) {
            //            更新列数据
            Filed filed = filedList.get(i);
            filed.setFiledDataSize(filed.getFiledDataSize()+
                    excelData.size());
            filedMapper.updateById(filed);
            //        创建一条操作行为实体类
            Execute execute = new Execute();
            execute.setCreatedBy(Processing.getAuthHeaderToUserId(request))
                    .setTableId(dataTable.getId())
                    .setFileId(filed.getId())
                    .setType(ExecuteActionTypeConstant.UPDATE);
            //        向数据库添加数据
            executeMapper.insert(execute);

            //        正向列遍历Excel数据
            for (int rnb = 0; rnb < excelData.size(); rnb++ ){

                List<String> rowData = excelData.get(rnb);

                String cellData = rowData.get(i);

                FiledData filedData = new FiledData();
                filedData.setData(cellData)
                        .setExecuteId(execute.getId())
                        .setFiledId(filed.getId())
                        .setCreatedTime(new Date())
                        .setIsDelete(0);
                filedDataList.add(filedData);

                if (filedDataList.size() >= 1000){
                    log.info("\t\t> 正在向Excel数据表【{}】插入了1000条数据,共计{}条",dataTable.getName(),(i+1)*(rnb+1) );
                    filedDataMapper.insertBatchSomeColumn(filedDataList);
                    filedDataList.clear();
                }
            }

        }

        if (!filedDataList.isEmpty()) {
            filedDataMapper.insertBatchSomeColumn(filedDataList);
            log.info("\t\t> 正在向Excel数据表【{}】插入了{}条数据",dataTable.getName(),filedDataList.size());
        }

//        添加记录
        Record record = new Record();
        record.setType("更新")
                .setContent("用户"+userDAO.getUserNameById(uid)+"向数据表" +
                        dataTable.getName()+"更新了数据")
                .setIp(IpUtils.getIpAddr(request))
                .setUserId(uid);
        recordMapper.insert(record);

        return ResultUtil.success("更新成功");
    }


    /**
     * 根据数据表id获取数据表内的所有数据
     *
     * @param id 数据表id
     * @param request 请求
     * @return com.modeling.utils.BaseResponse
     * @author zrx
     **/
    @Override
    public BaseResponse getTableDataById(Long id, HttpServletRequest request) {

//        获取用户id
        Long uid = Processing.getAuthHeaderToUserId(request);

//        获取对应数据表实体类
        if (!dataTableDAO.isExistDataTableById(id)) {
            return ResultUtil.error(ErrorCode.TABLE_DATA_NOT_EXIST);
        }
//        获取数据表和列数据
        DataTable dataTable = dataTableMapper.selectById(id);
        List<Filed> filedIdList = filedDAO.getFiledListByTableId(id);
//      定义数据总量的统计数
        Integer dataTotal = 0;
//        获取所有列数据
        List<FiledDataVO> filedDataVOS = new ArrayList<>();
        for (Filed filed : filedIdList) {
            FiledDataVO filedDataVO = new FiledDataVO();
            Processing.copyProperties(filed, filedDataVO);
            List<String> filedData = filedDataDAO.getFiledDataByFiledId(filed.getId());
            filedDataVO.setExcelData(filedData);
            filedDataVOS.add(filedDataVO);
            dataTotal += filedData.size();
            log.info("\t\t> 获取到了名为【{}】的数据表中列名为【{}】的数据",dataTable.getName()
                    ,filedDataVO.getName());
        }

        DataTableVO dataTableVO = new DataTableVO();
//        更新数据表的数据总数
        dataTable.setDataTotal(dataTotal);
        dataTableMapper.updateById(dataTable);

//        封装VO类
        Processing.copyProperties(dataTable,dataTableVO);
        dataTableVO.setFileds(filedDataVOS);

        Record record = new Record();
        record.setType("查询")
                .setContent("用户"+userDAO.getUserNameById(uid)+"查询了数据表" +
                        dataTableVO.getName()+"的所有数据")
                .setIp(IpUtils.getIpAddr(request))
                .setUserId(uid);
        recordMapper.insert(record);



        return ResultUtil.success("获取表内数据成功",dataTableVO);
    }


    /**
     * 分页 根据id获取数据表数据信息
     *
     * @param id 数据表id
     * @param request  请求
     * @return com.modeling.utils.BaseResponse
     * @author zrx
     **/
    @Override
    public BaseResponse getPageTableDataById(Long id,
                                             Integer page,
                                             Integer pageSize,
                                             HttpServletRequest request) {

        Long uid = Processing.getAuthHeaderToUserId(request);

//        获取对应数据表实体类
        if (!dataTableDAO.isExistDataTableById(id)) {
            return ResultUtil.error(ErrorCode.TABLE_DATA_NOT_EXIST);
        }

//        获取数据表和其所属的列信息
        DataTable dataTable = dataTableMapper.selectById(id);
        List<Filed> filedIdList = filedDAO.getFiledListByTableId(id);

//        分页获取数据表的数据
        List<FiledDataVO> filedDataVOS = new ArrayList<>();
        for (Filed filed : filedIdList) {
//            获取每个列数据，并封装VO类
            FiledDataVO filedDataVO = new FiledDataVO();
            Processing.copyProperties(filed, filedDataVO);
            filedDataVO.setExcelData(filedDataDAO.getPageFiledDataByFiledId(filed.getId(),page,pageSize));
            filedDataVOS.add(filedDataVO);
            log.info("\t\t> 获取第{}页{}条到了名为【{}】的数据表中列名为【{}】的数据",
                    page,pageSize, dataTable.getName(),filedDataVO.getName());
        }
//        封装数据表VO类
        DataTableVO dataTableVO = new DataTableVO();
        Processing.copyProperties(dataTable,dataTableVO);
        dataTableVO.setFileds(filedDataVOS);


//        增加记录
        Record record = new Record();
        record.setType("查询")
                .setContent("用户"+userDAO.getUserNameById(uid)+"查询了数据表" +
                        dataTableVO.getName()+"的第"+page+"页"+pageSize+"条的数据")
                .setIp(IpUtils.getIpAddr(request))
                .setUserId(uid);
        recordMapper.insert(record);

        return ResultUtil.success(dataTableVO);
    }


    /**
     * 删除数据表
     *
     * @param id 用户id
     * @param request  请求
     * @return com.modeling.utils.BaseResponse
     * @author zrx
     **/
    @Override
    public BaseResponse deleteDataTable(Long id, HttpServletRequest request) {

//        获取用户id
        Long uid = Processing.getAuthHeaderToUserId(request);

//        检测数据库表是否存在
        if (!dataTableDAO.isExistDataTableById(id)) {
            return ResultUtil.error(ErrorCode.DATA_TABLE_NOT_EXIST);
        }
        DataTable dataTable = dataTableMapper.selectById(id);

//        先删除列和列的数据
        List<Filed> filedList = filedDAO.getFiledListByTableId(id);
        for (Filed filed : filedList) {
            filedDataDAO.deleteExcelDataByFiledId(filed.getId());
            log.info("\t\t> 删除了名为【{}】的数据表中的【{}】列的数据",
                    dataTable.getName(),filed.getName());
            filedMapper.deleteById(filed.getId());
        }
//        再删除数据表
        dataTableMapper.deleteById(id);

//        增加记录
        Record record = new Record();
        record.setType("删除")
                .setContent("用户"+userDAO.getUserNameById(uid)+"删除了名为"+
                        dataTable.getName()+"的数据表")
                .setIp(IpUtils.getIpAddr(request))
                .setUserId(uid);
        recordMapper.insert(record);

        return ResultUtil.success("删除成功");
    }



    /**
     * 获取数据表的信息
     *
     * @param request 请求
     * @return com.modeling.utils.BaseResponse
     * @author zrx
     **/
    @Override
    public BaseResponse getTable(HttpServletRequest request) {

//        获取用户id
        Long uid = Processing.getAuthHeaderToUserId(request);

//        获取所有数据表信息
        List<DataTable> dataTableList = dataTableMapper.selectList(null);

//        封装数据表实体类
        List<DataTableVO> dataTableVOS = dataTableList.stream().map(dataTable -> {
            DataTableVO dataTableVO = new DataTableVO();
            Processing.copyProperties(dataTable, dataTableVO);
            return dataTableVO;
                }).collect(Collectors.toList());

//        获取每个数据表下的列信息
        for (DataTableVO dataTableVO : dataTableVOS) {
            List<Filed> filedIdList = filedDAO.getFiledListByTableId(dataTableVO.getId());
            List<FiledDataVO> filedDataVOS = new ArrayList<>();
            for (Filed filed : filedIdList) {
                FiledDataVO filedDataVO = new FiledDataVO();
                Processing.copyProperties(filed, filedDataVO);
                filedDataVOS.add(filedDataVO);
            }
            dataTableVO.setFileds(filedDataVOS);
        }

//        增加记录
        Record record = new Record();
        record.setType("查询")
                .setContent("用户"+userDAO.getUserNameById(uid)+"查询了所有数据表信息")
                .setIp(IpUtils.getIpAddr(request))
                .setUserId(uid);
        recordMapper.insert(record);

        return ResultUtil.success(dataTableVOS);
    }

}
