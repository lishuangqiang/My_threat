package com.example.my_theatre.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 全局跨域配置

 */
@Configuration
public class CorsConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // 允许跨域访问所有路径
                .allowedOrigins("http://localhost:5500") // 允许的源，可以根据需要修改
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // 允许的方法
                .allowedHeaders("*") // 允许的头
                .allowCredentials(true) // 允许凭证（如Cookies）
                .maxAge(3600); // 预请求的有效时间
    }
}