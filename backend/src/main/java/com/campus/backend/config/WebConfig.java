package com.campus.backend.config;

// WebConfig已完全禁用 - 让Spring Boot使用100%默认配置
// 原因：实现WebMvcConfigurer接口可能干扰了默认的HandlerMapping行为
// 导致有依赖的Controller无法被正确注册
//
// 如果需要自定义拦截器，请使用 @Configuration + @Bean 方式单独添加

// @Configuration
// public class WebConfig implements WebMvcConfigurer {
//     // 完全留空或删除此类
// }