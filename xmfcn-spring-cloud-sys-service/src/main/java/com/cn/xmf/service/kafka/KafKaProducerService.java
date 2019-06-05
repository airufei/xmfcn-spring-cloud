package com.cn.xmf.service.kafka;

import com.alibaba.fastjson.JSONObject;
import com.cn.xmf.util.StringUtil;
import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * kafka生产者
 *
 * @author rufei.cn
 * @version 2019-05-09
 */
@RestController
@RequestMapping(value = "/server/kafka/")
@SuppressWarnings("all")
public class KafKaProducerService {

    private static Logger logger = LoggerFactory.getLogger(KafKaProducerService.class);
    @Autowired
    private KafkaProducer<String, String> kafkaProducer;

    /**
     * sendKafka（发送kafka消息）
     *
     * topic 主题 必填
     * key 数据key 选填
     * value 数据主体 必填
     * @param jsonObject
     * @return
     */
    @RequestMapping("sendKafka")
    public boolean sendKafka(@RequestBody JSONObject jsonObject) {
        boolean result = false;
        if (jsonObject == null || jsonObject.size() <= 0) {
            return result;
        }
        String topic = jsonObject.getString("topic");
        String key = jsonObject.getString("key");
        String value = jsonObject.getString("value");
        if (StringUtil.isBlank(key)) {
            key = StringUtil.getUuId();
        }
        ProducerRecord<String, String> record = new ProducerRecord<>(topic, key, value);//Topic Key Value
        kafkaProducer.send(record, new Callback() {
            public void onCompletion(RecordMetadata metadata, Exception e) {
                if (e != null) {
                    e.printStackTrace();
                    logger.info(StringUtil.getExceptionMsg(e));
                }
            }
        });
        result = true;
        return result;
    }
}
