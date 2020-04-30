package com.composer.user;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import tk.mybatis.spring.annotation.MapperScan;

@SpringBootApplication
@EnableDiscoveryClient
@MapperScan("com.composer.user.mapper")
public class OlsUserApplication {
    public static void main(String[] args) {
        SpringApplication.run(OlsUserApplication.class);
    }
}
