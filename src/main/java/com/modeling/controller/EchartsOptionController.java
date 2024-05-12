package com.modeling.controller;

import com.modeling.service.EchartsService;
import com.modeling.utils.BaseResponse;
import com.modeling.utils.ErrorCode;
import com.modeling.utils.ResultUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 描述：
 *
 * @author zrx
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("echarts")
public class EchartsOptionController {

	private final EchartsService echartsService;

	/**
	 * 统计国内城市地址
	 *
	 * @param filedIds  多个列id
	 * @return com.modeling.utils.BaseResponse
	 * @author zrx
	 **/
	@GetMapping("/place")
	public BaseResponse getChinaPlaceDataByFiledIds(@RequestParam List<Long> filedIds) {

		if (filedIds == null || filedIds.size() == 0) {
			return ResultUtil.error(ErrorCode.PARAMETER_ERROR);
		}

		return echartsService.getChinaPlaceDataByFiledIds(filedIds);
	}

	/**
	 * 统计多个列内全球数据（国家为单位）
	 *
	 * @param filedIds  列id
	 * @return com.modeling.utils.BaseResponse
	 * @author zrx
	 **/
	@GetMapping("/allPlace")
	public BaseResponse getAllPlaceDataByFiledIds(@RequestParam List<Long> filedIds) {

		if (filedIds == null || filedIds.size() == 0) {
			return ResultUtil.error(ErrorCode.PARAMETER_ERROR);
		}

		return echartsService.getAllPlaceDataByFiledIds(filedIds);
	}


//	@GetMapping("linear")
//	public BaseResponse getLinearTotalByFiledId(@RequestParam Long xId,
//												@RequestParam Long yId) {
//		if (xId == null || yId == null) {
//			return ResultUtil.error(ErrorCode.PARAMETER_ERROR);
//		}
//
//		return echartsService.getLinearTotalByFiledId(xId, yId);
//	}
}



