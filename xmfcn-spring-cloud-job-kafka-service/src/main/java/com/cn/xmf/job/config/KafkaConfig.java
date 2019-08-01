package com.cn.xmf.job.config;

import com.alibaba.fastjson.JSONObject;
import com.cn.xmf.job.common.SysCommonService;
import com.cn.xmf.util.ConstantUtil;
import com.cn.xmf.util.StringUtil;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.util.Properties;

/**
 * kafka配置
 *
 * @author rufei.cn
 * @version 2019-05-19
 */
@Component
@SuppressWarnings("all")
public class KafkaConfig {

    private static final Logger logger = LoggerFactory.getLogger(KafkaConfig.class);

    private final Environment environment;
    private final SysCommonService sysCommonService;


    public KafkaConfig(Environment environment, SysCommonService sysCommonService) {
        this.environment = environment;
        this.sysCommonService = sysCommonService;
    }


    Properties initConsumerProp(String topic) {
        Properties consumerProps = new Properties();
        String kafkaServer = null;// kafka连接地址
        String consumerGroupId = null;//消费者组
        String autoOffsetReset = null;//earliest：从第一条消息开始消费；latest：从最新的一条开始消费；none：如果消费者组找到之前的offset，则向消费者抛出异常；anything else：抛出异常给消费者
        String consumerAutoCommit = null;//是否自动提交
        String consumerMaxPollIntervalMs = null;//两次poll()的间隔若大于该配置时间，分组将重新平衡，以便将分区重新分配给别的成员。
        String consumerMaxPollRecords = null;//单次拉取的最大消息数量
        String consumerSessionTimeoutMs = null;//如果消费者崩溃或无法在session.timeout.ms配置的时间内发送心跳，则消费者将被视为死亡
        String configKafka = ConstantUtil.DICT_TYPE_CONFIG_KAFKA;
        String kafkaConfig = null;
        if (sysCommonService != null && StringUtil.isNotBlank(topic)) {//优先加载topic配置
            kafkaConfig = sysCommonService.getDictValue(configKafka, topic);
        }
        if (StringUtil.isBlank(kafkaConfig) && sysCommonService != null) {//再选择公共字典配置，最后选择配置文件
            kafkaConfig = sysCommonService.getDictValue(configKafka, "kafka_common_config");
        }
        JSONObject json = JSONObject.parseObject(kafkaConfig);
        logger.info("============================》kafka配置 config_type={}，kafka_config={}：", configKafka, kafkaConfig);
        if (json != null) {
            kafkaServer = json.getString("kafka_server");
            autoOffsetReset = json.getString("auto_offset_reset");
            consumerAutoCommit = json.getString("consumer_autoCommit");
            consumerMaxPollIntervalMs = json.getString("consumer_maxPollIntervalMs");
            consumerMaxPollRecords = json.getString("consumer_maxPollRecords");
            consumerSessionTimeoutMs = json.getString("consumer_sessionTimeoutMs");
            consumerGroupId = json.getString("consumer_groupId");
        }
        if (StringUtil.isBlank(kafkaServer)) {
            kafkaServer = environment.getProperty("kafka.server");// kafka连接地址
        }
        if (StringUtil.isBlank(consumerGroupId)) {
            consumerGroupId = environment.getProperty("kafka.consumer.groupId");//消费者组
        }
        if (StringUtil.isBlank(autoOffsetReset)) {
            autoOffsetReset = environment.getProperty("kafka.consumer.auto_offset_reset");//earliest：从第一条消息开始消费,latest：从最新的一条开始消费
        }
        if (StringUtil.isBlank(consumerAutoCommit)) {
            consumerAutoCommit = environment.getProperty("kafka.consumer.enableAutoCommit");//是否自动提交
        }
        if (StringUtil.isBlank(consumerMaxPollIntervalMs)) {
            consumerMaxPollIntervalMs = environment.getProperty("kafka.consumer.maxPollIntervalMs");//两次poll()的间隔若大于该配置时间，分组将重新平衡
        }
        if (StringUtil.isBlank(consumerMaxPollRecords)) {
            consumerMaxPollRecords = environment.getProperty("kafka.consumer.maxPollRecords");//单次拉取的最大消息数量
        }
        if (StringUtil.isBlank(consumerSessionTimeoutMs)) {
            consumerSessionTimeoutMs = environment.getProperty("kafka.consumer.sessionTimeoutMs");//如果消费者崩溃或无法在session.timeout.ms配置的时间内发送心跳，则消费者将被视为死亡
        }
        consumerProps.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaServer);
        // 消费者组
        consumerProps.put(ConsumerConfig.GROUP_ID_CONFIG, consumerGroupId);
        // 当kafka consumer 第一次消费某个topic时，kafka中没有改consumer的offset，或者改offset被删除了，consumer该从什么地方开始消费
        // earliest：从第一条消息开始消费；latest：从最新的一条开始消费；none：如果消费者组找到之前的offset，则向消费者抛出异常；anything else：抛出异常给消费者
        consumerProps.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, autoOffsetReset);
        consumerProps.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, consumerAutoCommit);
        // 自动提交时间间隔
        // consumerProps.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, 1000);
        // 两次poll()的间隔若大于该配置时间，如果此超时时间期满之前poll()没有调用，则消费者被视为失败，并且分组将重新平衡，以便将分区重新分配给别的成员。
        consumerProps.put(ConsumerConfig.MAX_POLL_INTERVAL_MS_CONFIG, consumerMaxPollIntervalMs);
        // 单次拉取的最大消息数量
        consumerProps.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, consumerMaxPollRecords);
        // 如果消费者崩溃或无法在session.timeout.ms配置的时间内发送心跳，则消费者将被视为死亡，并且其分区将被重新分配。
        consumerProps.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, consumerSessionTimeoutMs);
        consumerProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, org.apache.kafka.common.serialization.StringDeserializer.class);
        consumerProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, org.apache.kafka.common.serialization.StringDeserializer.class);
        return consumerProps;
    }


}
