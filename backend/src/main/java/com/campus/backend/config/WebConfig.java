package com.campus.backend.config;

import com.campus.backend.common.interceptor.ApiVersionInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    
    @Autowired
    private ApiVersionInterceptor apiVersionInterceptor;
    
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(apiVersionInterceptor)
                .addPathPatterns("/api/**")
                .excludePathPatterns("/api-docs/**", "/swagger-ui/**", "/h2-console/**");
    }
    
    // 完全移除 addResourceHandlers 方法
    // 让Spring Boot使用默认的静态资源配置
    // 默认行为：
    // 1. 自动服务 classpath:/static/, classpath:/public/, classpath:/resources/, classpath:/META-INF/resources/
    // 2. 不干扰 Controller 的 @RequestMapping 映射
    // 3. 正确处理 /api/** 路径（Controller优先级高于静态资源）
}