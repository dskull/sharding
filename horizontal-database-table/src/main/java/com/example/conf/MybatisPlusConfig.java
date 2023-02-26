package com.example.conf;

import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author lixiaoqiang
 */
@Configuration
@MapperScan(value = "com.example.mapper")
public class MybatisPlusConfig {

    @Bean
    public PaginationInterceptor paginationInnerInterceptor() {
        return new PaginationInterceptor();
    }
}
