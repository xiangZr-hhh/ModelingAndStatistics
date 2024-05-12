package com.modeling.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.modeling.dao.RecordDAO;
import com.modeling.dao.UserDAO;
import com.modeling.mapper.RecordMapper;
import com.modeling.mapper.UpdatedLogMapper;
import com.modeling.model.entity.Record;
import com.modeling.model.entity.UpdatedLog;
import com.modeling.model.vodata.UpdatedLogAddVO;
import com.modeling.model.vodata.UpdatedLogReturnVO;
import com.modeling.service.UpdatedLogService;
import com.modeling.utils.BaseResponse;
import com.modeling.utils.IpUtils;
import com.modeling.utils.Processing;
import com.modeling.utils.ResultUtil;
import com.sun.net.httpserver.HttpServer;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * (UpdatedLog)表服务实现类
 *
 * @author zrx
 * @since 2024-05-12 17:33:06
 */
@Service("updatedLogService")
@RequiredArgsConstructor
public class UpdatedLogServiceImpl extends ServiceImpl<UpdatedLogMapper, UpdatedLog> implements UpdatedLogService {

	private final UpdatedLogMapper updatedLogMapper;
	private final RecordMapper recordMapper;

	private final UserDAO userDAO;
	private final RecordDAO recordDAO;

	/**
	 * 添加更新日志
	 *
	 * @param updatedLogAddVO
	 * @param request
	 * @return com.modeling.utils.BaseResponse
	 * @author zrx
	 **/
	@Override
	public BaseResponse addUpdatedLog(UpdatedLogAddVO updatedLogAddVO, HttpServletRequest request) {

		//		获取用户id
		Long uid = Processing.getAuthHeaderToUserId(request);

		String content = updatedLogAddVO.getContent();
		String type = updatedLogAddVO.getType();
		String version = updatedLogAddVO.getVersion();
		String moduleName = updatedLogAddVO.getModuleName();

		//		添加更新日志
		UpdatedLog updatedLog = new UpdatedLog();
		updatedLog.setType(type)
				.setModuleName(moduleName)
				.setContent(content)
				.setCreatedBy(uid)
				.setVersion(version)
				.setIp(IpUtils.getIpAddr(request));
		updatedLogMapper.insert(updatedLog);

		//        添加记录
		Record record = new Record();
		record.setType("更新日志")
				.setContent("用户"+userDAO.getUserNameById(uid)+"添加了更新日志")
				.setIp(IpUtils.getIpAddr(request))
				.setUserId(uid);
		recordMapper.insert(record);

		return ResultUtil.success("添加更新日志成功");
	}

	@Override
	public BaseResponse getAllUpdatedLog(HttpServletRequest request) {

		List<UpdatedLogReturnVO> logVO = recordDAO.getAllUpdateLogReturnVO();

		JSONObject jsonObject =new JSONObject();
		jsonObject.put("total",logVO);
		jsonObject.put("logData",logVO);

		return ResultUtil.success(jsonObject);
	}


}
