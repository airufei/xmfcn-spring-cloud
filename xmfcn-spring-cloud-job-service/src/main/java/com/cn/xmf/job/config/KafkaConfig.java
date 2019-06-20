package com.cn.xmf.job.config;

import com.alibaba.fastjson.JSONObject;
import com.cn.xmf.job.common.SysCommonService;
import com.cn.xmf.util.ConstantUtil;
import com.cn.xmf.util.StringUtil;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.Deserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
public class KafkaConfig {

    @Autowired
    private Environment environment;

    @Autowired
    private SysCommonService sysCommonService;

    private static final Logger logger = LoggerFactory.getLogger(KafkaConfig.class);

    public Properties initConsumerProp() throws ClassNotFoundException {
        Properties consumerProps = new Properties();
        String kafka_server = null;// kafka连接地址
        String consumer_groupId = null;//消费者组
        String auto_offset_reset = null;//earliest：从第一条消息开始消费；latest：从最新的一条开始消费；none：如果消费者组找到之前的offset，则向消费者抛出异常；anything else：抛出异常给消费者
        String consumer_autoCommit = null;//是否自动提交
        String consumer_maxPollIntervalMs = null;//两次poll()的间隔若大于该配置时间，分组将重新平衡，以便将分区重新分配给别的成员。
        String consumer_maxPollRecords = null;//单次拉取的最大消息数量
        String consumer_sessionTimeoutMs = null;//如果消费者崩溃或无法在session.timeout.ms配置的时间内发送心跳，则消费者将被视为死亡
        String config_type = ConstantUtil.DICT_TYPE_CONFIG_KAFKA;
        String kafka_config = sysCommonService.getDictValue(config_type, "kafka_common_config");
        JSONObject json = JSONObject.parseObject(kafka_config);
        logger.info("============================》kafka配置 config_type={}，kafka_config={}：", config_type, kafka_config);
        if (json != null) {
            kafka_server = json.getString("kafka_server");
            auto_offset_reset = json.getString("auto_offset_reset");
            consumer_autoCommit = json.getString("consumer_autoCommit");
            consumer_maxPollIntervalMs = json.getString("consumer_maxPollIntervalMs");
            consumer_maxPollRecords = json.getString("consumer_maxPollRecords");
            consumer_sessionTimeoutMs = json.getString("consumer_sessionTimeoutMs");
            consumer_groupId = json.getString("consumer_groupId");
        }
        if (StringUtil.isBlank(kafka_server)) {
            kafka_server = environment.getProperty("kafka.server");// kafka连接地址
        }
        if (StringUtil.isBlank(consumer_groupId)) {
            consumer_groupId = environment.getProperty("kafka.consumer.groupId");//消费者组
        }
        if (StringUtil.isBlank(auto_offset_reset)) {
            auto_offset_reset = environment.getProperty("kafka.consumer.auto_offset_reset");//earliest：从第一条消息开始消费,latest：从最新的一条开始消费
        }
        if (StringUtil.isBlank(consumer_autoCommit)) {
            consumer_autoCommit = environment.getProperty("kafka.consumer.enableAutoCommit");//是否自动提交
        }
        if (StringUtil.isBlank(consumer_maxPollIntervalMs)) {
            consumer_maxPollIntervalMs = environment.getProperty("kafka.consumer.maxPollIntervalMs");//两次poll()的间隔若大于该配置时间，分组将重新平衡
        }
        if (StringUtil.isBlank(consumer_maxPollRecords)) {
            consumer_maxPollRecords = environment.getProperty("kafka.consumer.maxPollRecords");//单次拉取的最大消息数量
        }
        if (StringUtil.isBlank(consumer_sessionTimeoutMs)) {
            consumer_sessionTimeoutMs = environment.getProperty("kafka.consumer.sessionTimeoutMs");//如果消费者崩溃或无法在session.timeout.ms配置的时间内发送心跳，则消费者将被视为死亡
        }
        consumerProps.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafka_server);
        // 消费者组
        consumerProps.put(ConsumerConfig.GROUP_ID_CONFIG, consumer_groupId);
        // 当kafka consumer 第一次消费某个topic时，kafka中没有改consumer的offset，或者改offset被删除了，consumer该从什么地方开始消费
        // earliest：从第一条消息开始消费；latest：从最新的一条开始消费；none：如果消费者组找到之前的offset，则向消费者抛出异常；anything else：抛出异常给消费者
        consumerProps.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, auto_offset_reset);
        consumerProps.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, consumer_autoCommit);
        // 自动提交时间间隔
        // consumerProps.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, 1000);
        // 两次poll()的间隔若大于该配置时间，如果此超时时间期满之前poll()没有调用，则消费者被视为失败，并且分组将重新平衡，以便将分区重新分配给别的成员。
        consumerProps.put(ConsumerConfig.MAX_POLL_INTERVAL_MS_CONFIG, consumer_maxPollIntervalMs);
        // 单次拉取的最大消息数量
        consumerProps.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, consumer_maxPollRecords);
        // 如果消费者崩溃或无法在session.timeout.ms配置的时间内发送心跳，则消费者将被视为死亡，并且其分区将被重新分配。
        consumerProps.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, consumer_sessionTimeoutMs);
        consumerProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, org.apache.kafka.common.serialization.StringDeserializer.class);
        consumerProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, org.apache.kafka.common.serialization.StringDeserializer.class);
        return consumerProps;
    }


}
