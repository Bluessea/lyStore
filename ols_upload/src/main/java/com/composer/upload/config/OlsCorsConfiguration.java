package com.composer.upload.config;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Component
public class OlsCorsConfiguration {

    @Bean
    public CorsFilter corsFilter(){
        //初始化cors配置对象
        CorsConfiguration configuration = new CorsConfiguration();

        //允许跨域的域名，如果要携带cookie则参数不能为* *代表所有域名都可以跨域访问
        configuration.addAllowedOrigin("http://manage.ols.com");
        configuration.setAllowCredentials(true);  //设置是否允许携带cookie
        configuration.addAllowedMethod("*"); //代表所有请求方法 GTP POST PUT DELETE..
        configuration.addAllowedHeader("*"); //允许携带任何头信息

        //初始化cors配置源对象
        UrlBasedCorsConfigurationSource corsConfigurationSource = new UrlBasedCorsConfigurationSource();
        corsConfigurationSource.registerCorsConfiguration("/**",configuration);
        //返回corsFilter实例，参数：cors配置源对象
        return new CorsFilter(corsConfigurationSource);
    }
}
