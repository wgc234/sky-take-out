package com.wgc.config;

import com.wgc.interceptor.JwtAdminInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;


@Configuration
@Slf4j
public class WebMvcConfiguration extends WebMvcConfigurationSupport {

    private final JwtAdminInterceptor jwtAdminInterceptor;

    @Autowired
    public WebMvcConfiguration(JwtAdminInterceptor jwtAdminInterceptor) {
        this.jwtAdminInterceptor = jwtAdminInterceptor;
    }

    @Override
    protected void addInterceptors(InterceptorRegistry registry) {
        log.info("开始注册拦截器...");
        registry.addInterceptor(jwtAdminInterceptor)
                .addPathPatterns("/admin/**")
                .excludePathPatterns("/admin/employee/login");
    }
}
