package com.cn.xmf.job;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * 任务处理
 *
 * @author rufei.cn
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
public class JobKafkaServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(JobKafkaServiceApplication.class, args);
    }
}
