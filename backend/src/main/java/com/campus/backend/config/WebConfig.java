package com.campus.backend.config;

import com.campus.backend.common.interceptor.ApiVersionInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
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
    
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 静态资源只匹配非API路径，避免拦截 /api/** 请求
        registry.addResourceHandler("/", "/index.html", "/404.html", "/assets/**", "/static/**", "/favicon.ico", "/vite.svg")
                .addResourceLocations("classpath:/static/")
                .setCachePeriod(3600);
    }
}
