package com.campus.backend;

import jakarta.annotation.PostConstruct;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.util.Map;

@SpringBootApplication
@MapperScan("com.campus.backend.mapper")
public class BackendApplication {

    @Autowired
    private ApplicationContext applicationContext;

    public static void main(String[] args) {
        SpringApplication.run(BackendApplication.class, args);
    }

    @PostConstruct
    public void logRegisteredControllers() {
        try {
            RequestMappingHandlerMapping handlerMapping = applicationContext
                    .getBean("requestMappingHandlerMapping", RequestMappingHandlerMapping.class);
            Map<RequestMappingInfo, Object> mappings = handlerMapping.getHandlerMethods();
            
            System.out.println("\n========================================");
            System.out.println("REGISTERED CONTROLLERS (" + mappings.size() + "):");
            System.out.println("========================================");
            
            mappings.forEach((info, handler) -> {
                String beanName = handler.getBeanType().getSimpleName();
                String paths = info.getPatternsCondition().getPatterns().toString();
                System.out.println("  ✅ " + beanName + " -> " + paths);
            });
            
            if (mappings.isEmpty()) {
                System.out.println("  ❌ NO CONTROLLERS REGISTERED!");
            }
            
            System.out.println("========================================\n");
        } catch (Exception e) {
            System.err.println("Failed to log controllers: " + e.getMessage());
        }
    }
}