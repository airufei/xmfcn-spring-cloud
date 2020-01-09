package com.cn.xmf.job.admin;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * 任务处理
 * @authorairufei
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableTransactionManagement// 开启事务
@EnableAspectJAutoProxy//开启AOP
@MapperScan(basePackages="com.cn.xmf.job.admin.*.dao")// 扫面mybatis Mapper包
@EnableFeignClients
public class JobAdminApplication {

    public static void main(String[] args) {
        SpringApplication.run(JobAdminApplication.class, args);
    }
}
