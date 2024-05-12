package com.modeling.service;

import com.modeling.utils.BaseResponse;

import java.util.List;

/**
 * echarts图像数据服务接口
 *
 * @author zrx
 * @since 2024-04-30 15:48:31
 */
public interface EchartsService {


	BaseResponse getChinaPlaceDataByFiledIds(List<Long> filedIds);

	BaseResponse getAllPlaceDataByFiledIds(List<Long> filedIds);


}
