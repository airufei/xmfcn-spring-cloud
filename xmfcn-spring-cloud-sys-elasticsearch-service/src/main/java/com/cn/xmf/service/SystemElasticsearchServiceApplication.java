package com.cn.xmf.service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * The type Service application.
 */
@SpringBootApplication
@EnableEurekaClient
@EnableFeignClients
public class SystemElasticsearchServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(SystemElasticsearchServiceApplication.class, args);
    }


}
