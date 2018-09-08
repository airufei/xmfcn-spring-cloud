package com.cn.xmf.job.admin;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * 任务处理
 * @authorairufei
 */
@SpringBootApplication
@EnableEurekaClient
@EnableTransactionManagement// 开启事务
@MapperScan(basePackages="com.cn.xmf.job.*.dao")// 扫面mybatis Mapper包
@EnableFeignClients
public class JobAdminApplication {

    public static void main(String[] args) {
        SpringApplication.run(JobAdminApplication.class, args);
    }
}
