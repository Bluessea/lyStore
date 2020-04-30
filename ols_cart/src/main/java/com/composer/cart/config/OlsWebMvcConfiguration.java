package com.composer.cart.config;

import com.composer.cart.interceptor.LoginInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 配置springMVC拦截器
 */
@Configuration
public class OlsWebMvcConfiguration implements WebMvcConfigurer {

    @Autowired
    private LoginInterceptor loginInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // /**表示拦截所有路径  /*表示拦截一级路径
        registry.addInterceptor(loginInterceptor).addPathPatterns("/**");
    }
}
