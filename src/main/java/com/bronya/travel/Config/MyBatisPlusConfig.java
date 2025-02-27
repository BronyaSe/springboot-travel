package com.bronya.travel.Config;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MyBatisPlusConfig {

    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        // 添加分页插件（适配不同数据库）
        PaginationInnerInterceptor paginationInterceptor = new PaginationInnerInterceptor();
        paginationInterceptor.setDbType(DbType.MYSQL); // 根据实际数据库类型配置
        paginationInterceptor.setMaxLimit(20L);  //限制单页请求的最大记录数
        paginationInterceptor.setOverflow(false);       // 溢出总页数后是否处理
        interceptor.addInnerInterceptor(paginationInterceptor);
        return interceptor;
    }
}
