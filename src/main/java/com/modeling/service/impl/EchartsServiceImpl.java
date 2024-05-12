package com.modeling.service.impl;

import com.modeling.dao.EchartsOptionDAO;
import com.modeling.dao.FiledDataDAO;
import com.modeling.mapper.FiledMapper;
import com.modeling.model.entity.Filed;
import com.modeling.service.EchartsService;
import com.modeling.utils.BaseResponse;
import com.modeling.utils.ResultUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * echarts图像数据服务实现类
 *
 * @author zrx
 * @since 2024-05-08 15:48:31
 */
@Slf4j
@Service("echartsService")
@RequiredArgsConstructor
public class EchartsServiceImpl implements EchartsService {

	private final FiledMapper filedMapper;

	private final EchartsOptionDAO echartsOptionDAO;


	/**
	 * 获取列数据里的国内地理信息统计
	 *
	 * @param filedIds  列id
	 * @return com.modeling.utils.BaseResponse
	 * @author zrx
	 **/
	@Override
	public BaseResponse getChinaPlaceDataByFiledIds(@RequestParam List<Long> filedIds) {

//		排除空的列
		for (Long filedId: filedIds) {
			Filed filed = filedMapper.selectById(filedId);
			if (filed == null) {
				filedIds.remove(filedId);
			}
		}

		return ResultUtil.success(echartsOptionDAO.getChinaPlaceOfOriginStatistics(filedIds));
	}


	/**
	 * 获取列数据里的全球地理信息统计（以国家为单位）
	 *
	 * @param filedIds  列id
	 * @return com.modeling.utils.BaseResponse
	 * @author zrx
	 **/
	@Override
	public BaseResponse getAllPlaceDataByFiledIds(@RequestParam List<Long> filedIds) {

//		排除空的列
		for (Long filedId: filedIds) {
			Filed filed = filedMapper.selectById(filedId);
			if (filed == null) {
				filedIds.remove(filedId);
			}
		}

		return ResultUtil.success(echartsOptionDAO.getAllPlaceOfOriginStatistics(filedIds));
	}


}


