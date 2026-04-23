package com.campus.backend.config;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * SPA (Single Page Application) Fallback控制器
 * 
 * Vue Router使用History模式时，直接访问 /login, /products 等路径
 * 需要服务端返回 index.html，由前端路由接管。
 * 
 * 关键：必须排除 /api/** 和其他后端路径，否则会拦截API请求！
 */
@Order(Ordered.LOWEST_PRECEDENCE)
@Controller
public class SpaFallbackController {

    @RequestMapping(value = {"/{path:^(?!api|assets|actuator).*$:[^\\.]*}"})
    public String fallback() {
        return "forward:/index.html";
    }
}