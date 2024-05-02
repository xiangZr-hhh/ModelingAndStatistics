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
import java.util.ArrayList;
import java.util.List;
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
        BaseResponse headerCheckResult = ExcelReadUtil.checkExcelSheetHeader(files);
        if (headerCheckResult.getCode() != 200) {
            return headerCheckResult;
        }

//        根据表头,设定数据表实体类的表头字段数据总数
        List<String> sheetHeader = (List<String>) headerCheckResult.getData();

//        检验Excel文件的数据格式
        BaseResponse dataCheckResult = ExcelReadUtil.checkExcelSheetData(files, sheetHeader);
        if (dataCheckResult.getCode() != 200) {
            return dataCheckResult;
        }
//        获取表头字段的类型
        List<String> sheetHeaderType = (List<String>) dataCheckResult.getData();
//        创建对应Excel数据表实体类
        DataTable dataTable = new DataTable();
        dataTable.setName(name);
        if (remark != null){
            dataTable.setRemark(remark);
        }
        dataTable.setCreatedBy(Processing.getAuthHeaderToUserId(request));
        dataTable.setFiledTotal(sheetHeader.size());
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
                    .setType(sheetHeaderType.get(k));
//            向数据库插入数据
            filedMapper.insert(sheetFiled);
            newFiledList.add(sheetFiled);
        }

//        获取Excel表数据
        List<List<String>> excelData = ExcelReadUtil.getAllExcelDataByHeaderName(files,
                                            sheetHeader);

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
        record.setType("插入")
                .setContent("用户"+userDAO.getUserNameById(uid)+"创建了数据表" +
                        dataTable.getName())
                .setIp(IpUtils.getIpAddr(request))
                .setUserId(uid);
        recordMapper.insert(record);

        return ResultUtil.success();
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

        Long uid = Processing.getAuthHeaderToUserId(request);

//        获取对应数据表实体类
        if (!dataTableDAO.isExistDataTableById(id)) {
            return ResultUtil.error(ErrorCode.TABLE_DATA_NOT_EXIST);
        }

        DataTable dataTable = dataTableMapper.selectById(id);
        List<Filed> filedIdList = filedDAO.getFiledListByTableId(id);

        List<FiledDataVO> filedDataVOS = new ArrayList<>();
        for (Filed filed : filedIdList) {
            FiledDataVO filedDataVO = new FiledDataVO();
            Processing.copyProperties(filed, filedDataVO);
            filedDataVO.setExcelData(filedDataDAO.getFiledDataByFiledId(filed.getId()));
            filedDataVOS.add(filedDataVO);
            log.info("\t\t> 获取到了名为【{}】的数据表中列名为【{}】的数据",dataTable.getName()
                    ,filedDataVO.getName());
        }
        DataTableVO dataTableVO = new DataTableVO();
        Processing.copyProperties(dataTable,dataTableVO);
        dataTableVO.setFileds(filedDataVOS);

        Record record = new Record();
        record.setType("查询")
                .setContent("用户"+userDAO.getUserNameById(uid)+"查询了数据表" +
                        dataTableVO.getName()+"的所有数据")
                .setIp(IpUtils.getIpAddr(request))
                .setUserId(uid);
        recordMapper.insert(record);

        return ResultUtil.success(dataTableVO);
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
