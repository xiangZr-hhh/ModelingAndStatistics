package com.modeling.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.modeling.mapper.UpdatedLogMapper;
import com.modeling.model.entity.UpdatedLog;
import com.modeling.model.vodata.UpdatedLogAddVO;
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

	@Override
	public BaseResponse addUpdatedLog(UpdatedLogAddVO updatedLogAddVO, HttpServletRequest request) {

		Long uid = Processing.getAuthHeaderToUserId(request);

		String content = updatedLogAddVO.getContent();
		String type = updatedLogAddVO.getType();
		String version = updatedLogAddVO.getVersion();

		UpdatedLog updatedLog = new UpdatedLog();
		updatedLog.setType(type)
				.setContent(content)
				.setCreatedBy(uid)
				.setVersion(version)
				.setIp(IpUtils.getIpAddr(request));

		updatedLogMapper.insert(updatedLog);

		return ResultUtil.success("添加更新日志成功");
	}
}
