package com.campus.backend.config;

import com.campus.backend.common.interceptor.ApiVersionInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class InterceptorConfig implements WebMvcConfigurer {
    
    @Autowired(required = false)
    private ApiVersionInterceptor apiVersionInterceptor;
    
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        if (apiVersionInterceptor != null) {
            registry.addInterceptor(apiVersionInterceptor)
                    .addPathPatterns("/api/**")
                    .excludePathPatterns("/api-docs/**", "/swagger-ui/**", "/h2-console/**");
        }
    }
}