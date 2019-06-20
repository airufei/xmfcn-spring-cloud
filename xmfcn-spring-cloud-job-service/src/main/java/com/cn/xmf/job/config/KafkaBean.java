package com.cn.xmf.job.config;

import com.cn.xmf.job.common.SysCommonService;
import com.cn.xmf.util.ConstantUtil;
import com.cn.xmf.util.StringUtil;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.util.Properties;

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
    public KafkaConsumer<String, String> getKafkaConsumer() {
        KafkaConsumer<String, String> kafkaConsumer = null;
        try {
            kafkaConsumer = new KafkaConsumer<>(kafkaConfig.initConsumerProp());
        } catch (ClassNotFoundException e) {
            logger.error("获取kafka 消费者异常======e={}", e);
            sysCommonService.sendDingMessage("KafkaBean.getKafkaProducer()", null, null, "获取kafka 消费者失败", getClass());
            e.printStackTrace();
        }
        return kafkaConsumer;
    }
}
