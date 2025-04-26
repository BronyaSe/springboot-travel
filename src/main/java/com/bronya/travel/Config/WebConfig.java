package com.bronya.travel.Config;

import com.bronya.travel.Interceptors.IsAdminInterceptors;
import com.bronya.travel.Interceptors.LoginInterceptors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Autowired
    private LoginInterceptors loginInterceptors;
    @Autowired
    private IsAdminInterceptors isAdminInterceptors;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(loginInterceptors).excludePathPatterns("/user/login","/user/register","/user/SendVerifyCode","/route/GetPage");
        registry.addInterceptor(isAdminInterceptors).addPathPatterns("/admin/**");
    }

}
