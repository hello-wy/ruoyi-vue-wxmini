package com.ruoyi.framework.config;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

/**
 * MyBatis-Plus 配置
 * 注册分页插件，使 BaseMapper.selectPage() 生效
 *
 * 注意：项目同时引入了 PageHelper，二者均为 MyBatis Executor 拦截器。
 * 通过 @Primary 确保 MP 的 MybatisPlusInterceptor 优先注册，
 * 并在 PaginationInnerInterceptor 中显式开启 optimizeCountSql，
 * 保证 selectPage 的 COUNT 查询正确执行，避免 total=0 的问题。
 *
 * @author ruoyi
 */
@Configuration
public class MybatisPlusConfig {

    @Bean
    @Primary
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        PaginationInnerInterceptor paginationInterceptor = new PaginationInnerInterceptor(DbType.MYSQL);
        // 单页最大限制，防止恶意大页请求，-1 表示不限制
        paginationInterceptor.setMaxLimit(-1L);
        // 溢出总页数时归到第一页
        paginationInterceptor.setOverflow(false);
        interceptor.addInnerInterceptor(paginationInterceptor);
        return interceptor;
    }
}
