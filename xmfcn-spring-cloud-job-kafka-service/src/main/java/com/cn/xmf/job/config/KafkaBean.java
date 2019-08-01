package com.cn.xmf.job.config;

import com.cn.xmf.job.common.SysCommonService;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

/**
 * kafka 消费者和生产者配置
 *
 * @author rufei.cn
 * @version 2019-05-19
 */
@Component
@SuppressWarnings("all")
public class KafkaBean {

    private static final Logger logger = LoggerFactory.getLogger(KafkaBean.class);
    @Autowired
    private SysCommonService sysCommonService;
    @Autowired
    private KafkaConfig kafkaConfig;
    @Autowired
    private Environment environment;

    /**
     * 获取kafka 消费者
     *
     * @return the consumer instance
     */
    public KafkaConsumer<String, String> getKafkaConsumer(String topic) {
        KafkaConsumer<String, String> kafkaConsumer = null;
        try {
            kafkaConsumer = new KafkaConsumer<>(kafkaConfig.initConsumerProp(topic));
        } catch (Exception e) {
            logger.error("获取kafka 消费者异常======e={}", e);
            sysCommonService.sendDingMessage("KafkaBean.getKafkaProducer()", null, null, "获取kafka 消费者失败", getClass());
        }
        return kafkaConsumer;
    }
}
