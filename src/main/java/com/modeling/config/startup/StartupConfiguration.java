package com.modeling.config.startup;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.modeling.common.SafeConstants;
import com.modeling.model.entity.Config;
import com.modeling.model.vodata.InfoAboutSecurityKey;
import com.modeling.utils.Processing;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;

/**
 * 系统启动时进行的一些初始化操作
 * <hr/>
 * 1. 检查数据库完整性
 * 2. 检查系统配置
 * 3. 检查系统权限
 * 4. 检查系统数据
 *
 * @author xiao_lfeng
 * @version v1.2.0
 * @since v1.2.0
 */
@Slf4j
@Configuration
@RequiredArgsConstructor
public class StartupConfiguration {
    private final JdbcTemplate jdbcTemplate;


    /**
     * 准备安全密钥
     * <hr/>
     * 准备安全密钥，用于加密解密等操作
     */
    @Bean
    @Order(1)
    public CommandLineRunner prepareKey() {
        return args -> {
            log.info("[Preparation] 系统进行安全密钥准备");
            Gson gson = new Gson();
            // 获取数据库中的安全密钥
            String getSecurityKey = jdbcTemplate.queryForObject(
                    "SELECT data FROM modeling.config WHERE name = 'security_key' LIMIT 1",
                    String.class);
            if (getSecurityKey != null) {
                HashMap<String, String> getData = gson.fromJson(
                        getSecurityKey,
                        new TypeToken<HashMap<String, String>>() {
                        }.getType());
                SafeConstants.setSecretKey(getData.get("key"));
            } else {
                // 生成密钥
                String key = Processing.generateKey(System.currentTimeMillis());
                InfoAboutSecurityKey infoAboutSecurityKey = new InfoAboutSecurityKey();
                infoAboutSecurityKey.setKey(key)
                        .setUpdateTime(System.currentTimeMillis());
                String json = gson.toJson(infoAboutSecurityKey, InfoAboutSecurityKey.class);
                // 更新密钥
                Config config = new Config();
                config.setName("security_key")
                        .setData(json);

                // 初始化密钥
                jdbcTemplate.update("INSERT INTO modeling.config (name, data) VALUES (?, ?)",
                        config.getName(),
                        config.getData()
                );
                SafeConstants.setSecretKey(key);
            }
        };

    }


}
