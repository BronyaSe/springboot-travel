package com.bronya.travel.Config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // 允许所有接口跨域
                .allowedOrigins("*") // 允许所有来源（生产环境建议指定具体域名）
                .allowedHeaders("*")// 显式声明允许的头
                .allowedMethods("OPTIONS","PUT","GET","DELETE","POST") // 允许的 HTTP 方法
                .allowCredentials(false) // 是否允许携带 Cookie（设置为 true 时，allowedOrigins 不能为 *）
                .maxAge(3600); // 预检请求的缓存时间（单位：秒）
    }
}
