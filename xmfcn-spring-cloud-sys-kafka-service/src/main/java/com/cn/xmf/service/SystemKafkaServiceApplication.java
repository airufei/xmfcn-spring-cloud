package com.cn.xmf.service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * The type Service application.
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
public class SystemKafkaServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(SystemKafkaServiceApplication.class, args);
    }


}
