package com.composer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import tk.mybatis.spring.annotation.MapperScan;

@SpringBootApplication
@EnableDiscoveryClient
@MapperScan("com.composer.item.mapper")
public class OlsItemApplication {
    public static void main(String[] args) {
        SpringApplication.run(OlsItemApplication.class);
    }
}
