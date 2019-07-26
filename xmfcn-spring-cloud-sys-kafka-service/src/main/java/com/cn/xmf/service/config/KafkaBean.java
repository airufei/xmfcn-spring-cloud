package com.cn.xmf.service.config;

import com.cn.xmf.util.StringUtil;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.util.Properties;

/**
 * kafka 生产者配置
 *
 * @author rufei.cn
 * @date 2019-05-20
 */
@Component
public class KafkaBean {

    private static final Logger logger = LoggerFactory.getLogger(KafkaBean.class);
    @Autowired
    private Environment environment;

    /**
     * 初始化信息
     *
     * @return
     */
    private Properties initProducerProp() throws ClassNotFoundException {
        Properties producerProps = new Properties();
        String kafka_server = environment.getProperty("kafka.server");
        producerProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafka_server);
        // 是判别请求是否为完整的条件（就是是判断是不是成功发送了）。我们指定了“all”将会阻塞消息，这种设置性能最低，但是是最可靠的。
        producerProps.put(ProducerConfig.ACKS_CONFIG, environment.getProperty("kafka.producer.ack"));
        // 如果请求返回可重试的错误，例如首领选举或网络连接异常等可在几秒钟内解决，生产者会自动发起重试。
        producerProps.put(ProducerConfig.RETRIES_CONFIG, environment.getProperty("kafka.producer.retries"));
        // 缓存大小，值较大的话将会产生更大的批。并需要更多的内存（因为每个“活跃”的分区都有1个缓冲区）
        producerProps.put(ProducerConfig.BATCH_SIZE_CONFIG, environment.getProperty("kafka.producer.batchSize"));
        // 默认缓冲可立即发送，即便缓冲空间还没有满，但是，如果你想减少请求的数量，可以设置linger.ms大于0。
        // 这将指示生产者发送请求之前等待一段时间，希望更多的消息填补到未满的批中。
        // 需要注意的是，在高负载下，相近的时间一般也会组成批，即使是 linger.ms=0。
        // 在不处于高负载的情况下，如果设置比0大，以少量的延迟代价换取更少的，更有效的请求。
        producerProps.put(ProducerConfig.LINGER_MS_CONFIG, environment.getProperty("kafka.producer.lingerMs"));
        // 控制生产者可用的缓存总量，如果消息发送速度比其传输到服务器的快，将会耗尽这个缓存空间。当缓存空间耗尽，
        // 其他发送调用将被阻塞，阻塞时间的阈值通过max.block.ms设定，之后它将抛出一个TimeoutException。
        producerProps.put(ProducerConfig.BUFFER_MEMORY_CONFIG, environment.getProperty("kafka.producer.bufferMemory"));
        producerProps.put(ProducerConfig.COMPRESSION_TYPE_CONFIG, environment.getProperty("kafka.producer.compressionType"));
        producerProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, Class.forName(environment.getProperty("kafka.producer.keySerializerClass")));
        producerProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, Class.forName(environment.getProperty("kafka.producer.valueSerializerClass")));
        return producerProps;
    }

    /**
     * 获取kafka 生产者
     *
     * @return the produver instance
     */
    @Bean
    public KafkaProducer<String, String> kafkaProducer() {
        KafkaProducer<String, String> kafkaProducer = null;
        try {
            kafkaProducer = new KafkaProducer<>(initProducerProp());
        } catch (ClassNotFoundException e) {
            logger.error("获取kafka 生产者异常======e={}", StringUtil.getExceptionMsg(e));
        }
        catch (Exception e) {
            logger.error("获取kafka 生产者异常======e={}", StringUtil.getExceptionMsg(e));
        }
        return kafkaProducer;
    }
}
