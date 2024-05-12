package com.modeling.dao;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.modeling.mapper.RecordMapper;
import com.modeling.mapper.UserMapper;
import com.modeling.model.entity.Record;
import com.modeling.model.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.time.temporal.TemporalAdjusters;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 描述：处理记录数据相关请求
 *
 * @author zrx
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class RecordDAO {

    private final UserDAO userDAO;
    private final RecordMapper recordMapper;

    private final LambdaQueryWrapper<Record> recordLambdaQueryWrapper = new LambdaQueryWrapper<>();

    /**
     * 获取本周活跃用户
     *
     * @return java.util.List<com.modeling.model.entity.User>
     * @author zrx
     **/
    public Map<String, Integer> getWeekActiveUserNumber () {
        // 获取本周第一天的日期时间
        LocalDateTime startOfWeek = LocalDate.now().with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY)).atStartOfDay();
        // 获取本周最后一天的日期时间
        LocalDateTime endOfWeek = LocalDate.now().with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY)).atTime(LocalTime.MAX);

        // 构建查询条件：创建时间在本周范围内
        recordLambdaQueryWrapper.clear();
        recordLambdaQueryWrapper.between(Record::getCreatedTime, startOfWeek, endOfWeek)
                .orderByAsc(Record::getCreatedTime);

        // 查询数据库，获取本周的记录列表
        List<Record> records = recordMapper.selectList(recordLambdaQueryWrapper);

        // 根据创建时间和IP地址进行去重(同一ip，一天多次操作算为一次)
        records =  records.stream()
                        .collect(Collectors.toMap(
                                record -> record.getCreatedTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDate() + "-" + record.getIp(), // 以日期和IP地址组合为key
                                Function.identity(), // 保留当前记录
                                (existing, replacement) -> existing // 如果有重复的key，保留已存在的记录
                        ))
                        .values().stream()
                        .collect(Collectors.toList());

        // 初始化一个映射，用于存储每天的活跃用户数量
        Map<String, Integer> dailyActiveUserNumbers = new HashMap<>();
        // 初始化每天的活跃用户数量为 0
        for (DayOfWeek dayOfWeek : DayOfWeek.values()) {
            dailyActiveUserNumbers.put(dayOfWeek.getDisplayName(TextStyle.FULL, Locale.CHINA), 0); // 将英文星期几转换为汉字表示
        }

        // 统计每天的活跃用户数量
        for (Record record : records) {
            Instant instant = record.getCreatedTime().toInstant();
            LocalDate recordDate = instant.atZone(ZoneId.systemDefault()).toLocalDate();
            DayOfWeek dayOfWeek = recordDate.getDayOfWeek();
            // 每天活跃用户数量加一
            dailyActiveUserNumbers.put(dayOfWeek.getDisplayName(TextStyle.FULL, Locale.CHINA), dailyActiveUserNumbers.get(dayOfWeek.getDisplayName(TextStyle.FULL, Locale.CHINA)) + 1);
        }

        return dailyActiveUserNumbers;
    }


    /**
     * 获取所有记录信息
     *
     * @return java.util.List<java.lang.String>
     * @author zrx
     **/
    public List<String> getAllRecordInfo() {

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        储存所有记录信息
        List<String> allRecordInfo = new ArrayList<>();
//        获取所有用户操作记录
        List<Record> records =recordMapper.selectList(null);
//        按创建时间排序
        records = records.stream()
                .sorted(Comparator.comparing(Record::getCreatedTime).reversed())
                .collect(Collectors.toList());
//      封装操作信息
        for (Record record:records) {
            StringBuilder recordInfo = new StringBuilder();
            recordInfo.append(format.format(record.getCreatedTime()))
                    .append("   ")
                    .append(record.getContent())
                    .append("(ip地址为:")
                    .append(record.getIp()+")");
            allRecordInfo.add(recordInfo.toString());
        }

        return allRecordInfo;
    }


    /**
     * 获取总使用人数
     *
     * @return java.lang.Integer
     * @author zrx
     **/
    public Integer getAllActiveUser () {
//        获取所有用户
        List<Record> records = recordMapper.selectList(null);
//        根据ip查重，去除本地ip，获取真实使用用户
        records = records.stream()
                .filter(record -> !record.getIp().equals("0:0:0:0:0:0:0:1"))
                .distinct()
                .collect(Collectors.toList());

        return records.size();
    }


    /**
     * 获取今日活跃用户
     *
     * @return java.lang.Integer
     * @author zrx
     **/
    public Integer getTodayActiveUser() {
//      获取今天的日期
        LocalDate today = LocalDate.now();
//        获取所有用户
        List<Record> records = recordMapper.selectList(null);
//        根据ip查重，去除本地ip，获取真实使用用户
        records = records.stream()
                .filter(record -> !record.getIp().equals("0:0:0:0:0:0:0:1"))
                .filter(record -> record.getCreatedTime().toInstant()
                        .atZone(ZoneId.systemDefault()).toLocalDate().equals(today))
                .distinct()
                .collect(Collectors.toList());

        return records.size();
    }

}


