package com.cn.xmf.config;

import ch.qos.logback.classic.spi.LoggingEvent;
import ch.qos.logback.core.AppenderBase;
import com.cn.xmf.util.ConstantUtil;
import com.cn.xmf.util.StringUtil;
import com.cn.xmf.util.TreadPoolUtil;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.sleuth.Tracer;

import java.util.Properties;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 扩展logback Appender 类 XmfLogAppender 实现log数据直接发送kafka队列
 *
 * @author rufei.cn
 */
@SuppressWarnings("all")
public class XmfLogAppender extends AppenderBase<LoggingEvent> {

    private static ThreadPoolExecutor cachedThreadPool = TreadPoolUtil.getCommonThreadPool();//获取公共线程池
    private static final Logger logger = LoggerFactory.getLogger(XmfLogAppender.class);
    private static Properties props;
    private static String topic = ConstantUtil.XMF_KAFKA_TOPIC_LOG;//日志主题

    private String subSysName;
    private String kafkaAddress;
    private Tracer tracer; // 默认注入的是DefaultTracer
    private Producer<String, String> producer;

    /**
     * 初始化
     */
    @Override
    public void start() {
        super.start();
        logger.info("Starting KafkaAppender...");
        try {
            props = new Properties();
            props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaAddress);
            // 是判别请求是否为完整的条件（就是是判断是不是成功发送了）。我们指定了“all”将会阻塞消息，这种设置性能最低，但是是最可靠的。
            props.put(ProducerConfig.ACKS_CONFIG, "-1");
            // 如果请求返回可重试的错误，例如首领选举或网络连接异常等可在几秒钟内解决，生产者会自动发起重试。
            props.put(ProducerConfig.RETRIES_CONFIG, 0);
            // 缓存大小，值较大的话将会产生更大的批。并需要更多的内存（因为每个“活跃”的分区都有1个缓冲区）
            props.put(ProducerConfig.BATCH_SIZE_CONFIG, 16384);
            // 默认缓冲可立即发送，即便缓冲空间还没有满，但是，如果你想减少请求的数量，可以设置linger.ms大于0。
            // 这将指示生产者发送请求之前等待一段时间，希望更多的消息填补到未满的批中。
            // 需要注意的是，在高负载下，相近的时间一般也会组成批，即使是 linger.ms=0。
            // 在不处于高负载的情况下，如果设置比0大，以少量的延迟代价换取更少的，更有效的请求。
            props.put(ProducerConfig.LINGER_MS_CONFIG, 100);
            // 控制生产者可用的缓存总量，如果消息发送速度比其传输到服务器的快，将会耗尽这个缓存空间。当缓存空间耗尽，
            // 其他发送调用将被阻塞，阻塞时间的阈值通过max.block.ms设定，之后它将抛出一个TimeoutException。
            props.put(ProducerConfig.BUFFER_MEMORY_CONFIG, 33554432);
            props.put(ProducerConfig.MAX_REQUEST_SIZE_CONFIG, 104857600);//
            props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
            props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
            producer = new KafkaProducer<>(props);
        } catch (Exception exception) {
            logger.error("KafkaAppender: Exception initializing Producer. " + exception + " : " + exception.getMessage());
            exception.printStackTrace();
        }
    }

    /**
     * 异步执行写入日志
     * @param loggingEvent
     */
    @Override
    protected void append(LoggingEvent loggingEvent) {
        TreadPoolUtil.getThreadPoolIsNext(cachedThreadPool,this.getClass());
        cachedThreadPool.execute(() -> start(loggingEvent));//异步执行
    }


    /**
     * 获取日志信息，写入kafka队列
     *
     * @param loggingEvent
     */
    public void start(LoggingEvent loggingEvent) {
        try {
            if (producer == null) {
                return;
            }
            String loggerName = loggingEvent.getLoggerName();
            String message = loggingEvent.getFormattedMessage();
            if (!loggerName.startsWith("com.cn.xmf")) {
                return;
            }
            String jsonString = StringUtil.getLogData(loggingEvent, subSysName);
            if (StringUtil.isBlank(jsonString)) {
                return;
            }
            // 方法是异步的，添加消息到缓冲区等待发送，并立即返回。生产者将单个的消息批量在一起发送来提高效率。
            producer.send(new ProducerRecord<>(ConstantUtil.XMF_KAFKA_TOPIC_LOG, topic, jsonString));
        } catch (Exception e) {
            logger.error(StringUtil.getExceptionMsg(e));

        }
    }

    public String getSubSysName() {
        return subSysName;
    }

    public void setSubSysName(String subSysName) {
        this.subSysName = subSysName;
    }

    public String getKafkaAddress() {
        return kafkaAddress;
    }

    public void setKafkaAddress(String kafkaAddress) {
        this.kafkaAddress = kafkaAddress;
    }
}
