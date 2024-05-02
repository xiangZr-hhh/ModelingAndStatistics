package com.modeling.config.mybatis_plus;

import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
 
@Component
public class MybatisPlusConfig {
    /********************************************************************************
     ** @author ： ZYJ
     ** @date ：2023/04/14
     ** @description ：注入配置
     *********************************************************************************/
    @Bean
    public InsertBatchSqlInjector easySqlInjector () {
        return new InsertBatchSqlInjector();
    }


    /**
     * IPage的分页使用的是拦截器，属于物理分页，好处就是处理大量数据时，查询速度快。
     *
     * @return MybatisPlus拦截器
     */
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        // 向MybatisPlus拦截器链中添加分页拦截器
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor());
        return interceptor;
    }



}