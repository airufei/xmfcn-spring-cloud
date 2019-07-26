package com.cn.xmf.service;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * The type Service application.
 */
@SpringBootApplication
@EnableEurekaClient
@EnableTransactionManagement// 开启事务
@EnableFeignClients
@MapperScan(basePackages = "com.cn.xmf.service.*.dao")// 扫面mybatis Mapper包
public class SystemBaseServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(SystemBaseServiceApplication.class, args);
    }

}
