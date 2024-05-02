package com.modeling.schedule;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.google.gson.Gson;
import com.modeling.mapper.ConfigMapper;
import com.modeling.model.entity.Config;
import com.modeling.model.vodata.InfoAboutSecurityKey;
import com.modeling.utils.Processing;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;

/**
 * <h1>安全相关日程</h1>
 * <hr/>
 * 用于安全相关的日程
 *
 * @since v1.1.0
 * @version v1.1.0
 * @author 筱锋xiao_lfeng
 */
@Component
@RequiredArgsConstructor
public class SafeSchedule {

    private final ConfigMapper configMapper;
    /**
     * <h2>更新密钥</h2>
     * <hr/>
     * 用于更新密钥，密钥每个月进行更新，需要检查每月是否到1号<br/>
     * 每天凌晨0点执行一次，从数据库获取数据检查是否需要更新密钥
     */
    @Scheduled(cron = "0 0 0 * * ?")
    public void updateKey() {
        // 获取当前日
        SimpleDateFormat getDateFormatAtDay = new SimpleDateFormat("dd");
        String day = getDateFormatAtDay.format(System.currentTimeMillis());
        if (day.equals("01")) {
            // 获取密钥及更新时间
            LambdaQueryWrapper<Config> configLambdaQueryWrapper =
                    new LambdaQueryWrapper<>();
            configLambdaQueryWrapper.eq(Config::getName,"security_key");
            Config config = configMapper.selectOne(configLambdaQueryWrapper);
            Gson gson = new Gson();
            InfoAboutSecurityKey securityKey = gson.fromJson(config.getData(), InfoAboutSecurityKey.class);
            // 检查时间戳是否超过了20天有效期
            if (System.currentTimeMillis() - securityKey.getUpdateTime() > 1728000000) {
                // 生成新密钥
                String newKey = Processing.generateKey(System.currentTimeMillis());
                // 更新密钥
                securityKey.setKey(newKey)
                        .setUpdateTime(System.currentTimeMillis());
                // 更新数据库
                config.setData(gson.toJson(securityKey));
                configMapper.updateById(config);
            }
        }
    }
}
