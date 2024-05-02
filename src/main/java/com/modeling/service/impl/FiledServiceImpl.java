package com.modeling.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.modeling.dao.DataTableDAO;
import com.modeling.dao.FiledDAO;
import com.modeling.dao.FiledDataDAO;
import com.modeling.dao.UserDAO;
import com.modeling.mapper.DataTableMapper;
import com.modeling.mapper.FiledMapper;
import com.modeling.mapper.RecordMapper;
import com.modeling.model.entity.DataTable;
import com.modeling.model.entity.Filed;
import com.modeling.model.entity.Record;
import com.modeling.model.vodata.DataTableVO;
import com.modeling.model.vodata.FiledDataVO;
import com.modeling.service.FiledService;
import com.modeling.utils.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * (Filed)表服务实现类
 *
 * @author zrx
 * @since 2024-04-30 20:37:52
 */
@Slf4j
@Service("filedService")
@RequiredArgsConstructor
public class FiledServiceImpl extends ServiceImpl<FiledMapper, Filed> implements FiledService {

    private final UserDAO userDAO;
    private final FiledDataDAO filedDataDAO;

    private final FiledMapper filedMapper;
    private final DataTableMapper dataTableMapper;
    private final RecordMapper recordMapper;

    /**
     * 根据列id获取数据
     *
     * @param filedIds 列id
     * @param request  请求
     * @return com.modeling.utils.BaseResponse
     * @author zrx
     **/
    @Override
    public BaseResponse getDataByFiledIdList(List<Long> filedIds, HttpServletRequest request) {

//        获取用户id
        Long uid = Processing.getAuthHeaderToUserId(request);

//        根据列id获取列的实体类
        List<Filed> filedList = filedIds.stream().map(id -> {
            return filedMapper.selectById(id);
        }).collect(Collectors.toList());

//        检测每个实体类的数据类型是否相同
        String firstType = filedList.get(0).getType(); // 获取第一个实体类的数据类型
        boolean checkType = filedList.stream()
                .allMatch(filed -> filed.getType().equals(firstType));
        if (!checkType) {
            return ResultUtil.error(ErrorCode.FILED_TYPE_NOT_TRUE);
        }

//        获取每个列的数据
        List<String> allFiledData = new ArrayList<>();
        for (Filed filed:filedList) {

            if (filed == null) {
                return ResultUtil.error(ErrorCode.FILED_NOT_EXIST);
            }

            DataTable dataTable = dataTableMapper.selectById(filed.getId());
            List<String> filedData;
            filedData = filedDataDAO.getFiledDataByFiledId(filed.getId());
            allFiledData.addAll(filedData);
            log.info("\t\t> 获取了数据表名为【{}】列名为中列名为【{}】的一列数据",
                 dataTable.getName(),filed.getName());
        }

//      增加记录
        Record record = new Record();
        record.setType("查询")
                .setContent("用户"+userDAO.getUserNameById(uid)+"查询了列id为"+
                        filedIds+"的列信息")
                .setIp(IpUtils.getIpAddr(request))
                .setUserId(uid);
        recordMapper.insert(record);

        return ResultUtil.success(allFiledData);
    }




}
