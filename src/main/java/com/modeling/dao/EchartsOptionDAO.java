package com.modeling.dao;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.modeling.common.PlaceJsonConstants;
import com.modeling.utils.Processing;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.lang.reflect.Type;
import java.util.*;

/**
 * 描述：处理echarts图表相应的请求
 *
 * @author zrx
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class EchartsOptionDAO {

	private final FiledDataDAO filedDataDAO;

	/**
	 * 获取国内归属地
	 *
	 * @param filedIds 列id数组
	 * @return java.util.Map<java.lang.String,java.lang.Integer>
	 * @author zrx
	 **/
	public JSONArray getChinaPlaceOfOriginStatistics (List<Long> filedIds) {
		Map<String, Integer> statisticsMap = new HashMap<>();
		Map<String, List<String>> placeData = parsePlaceJson(PlaceJsonConstants.PLACE_JSON);

		List<String> allData = new ArrayList<>();
		for (Long filedId : filedIds) {
			List<String> fieldData = filedDataDAO.getFiledDataByFiledId(filedId);
			allData.addAll(fieldData);
		}


		for (String city : allData) {
			if (city == null || city.equals("")) {
				continue;
			}
			// 检查是否为省份，如果是，则进行匹配
			boolean isProvince = false;
			for (String province : placeData.keySet()) {
				if (province.contains(city)) {
					statisticsMap.put(province, statisticsMap.getOrDefault(province, 0) + 1);
					isProvince = true;
					break;
				}
			}
			// 如果不是省份，则按照城市名统计
			if (!isProvince) {
				for (List<String> citys:placeData.values()){
					for (String cityName :citys) {
						if (cityName.equals(city)) {
							statisticsMap.put(city, statisticsMap.getOrDefault(city, 0) + 1);
							break;
						}
					}
				}
			}
		}

		JSONArray jsonArray = new JSONArray();
		for (Map.Entry<String, Integer> entry : statisticsMap.entrySet()) {
			JSONObject jsonObject = new JSONObject();
			String key = entry.getKey();
			Integer value = entry.getValue();
			jsonObject.put("name",key);
			jsonObject.put("value",value);
			jsonArray.add(jsonObject);
		}

		return jsonArray;
	}


	/**
	 * 统计国际位置
	 *
	 * @param filedIds 列id数组
	 * @return java.util.Map<java.lang.String,java.lang.Integer>
	 * @author zrx
	 **/
	public JSONArray getAllPlaceOfOriginStatistics (List<Long> filedIds) {
		Map<String, Integer> statisticsMap = new HashMap<>();
		Map<String, List<String>> placeData = parsePlaceJson(PlaceJsonConstants.PLACE_JSON);

		List<String> allData = new ArrayList<>();
		for (Long filedId : filedIds) {
			List<String> fieldData = filedDataDAO.getFiledDataByFiledId(filedId);
			allData.addAll(fieldData);
		}


		for (String city : allData) {
			if (city == null || city.equals("") || city.equals("未知")) {
				continue;
			}
			// 检查是否为省份，如果是，则进行匹配
			boolean isProvince = false;
			for (String province : placeData.keySet()) {
				if (province.contains(city) || city.contains("中国")) {
					statisticsMap.put("中国", statisticsMap.getOrDefault("中国", 0) + 1);
					isProvince = true;
					break;
				}
			}
			// 如果不是省份，则按照城市名统计
			if (!isProvince) {
				boolean isChinaCity = false;
				for (List<String> citys:placeData.values()){
					for (String cityName :citys) {
						if (cityName.equals(city)) {
							statisticsMap.put("中国", statisticsMap.getOrDefault("中国", 0) + 1);
							isChinaCity = true;
							break;
						}
					}
				}
				//如果也不是国内城市，说明其为国外地区
				if (!isChinaCity) {
					statisticsMap.put(city, statisticsMap.getOrDefault(city, 0) + 1);
				}
			}
		}

//		statisticsMap.remove("中国");

		Map<String, Integer> sortMap = Processing.sortMapByValue(statisticsMap);

		JSONArray jsonArray = new JSONArray();
		jsonArray.add(sortMap.keySet());
		jsonArray.add(sortMap.values());

		return jsonArray;
	}


	// 解析地理数据JSON字符串,返回城市列表
	private Map<String, List<String>> parsePlaceJson(String json) {
		Gson gson = new Gson();
		Type type = new TypeToken<Map<String, List<String>>>(){}.getType();
		return gson.fromJson(json, type);
	}

}


