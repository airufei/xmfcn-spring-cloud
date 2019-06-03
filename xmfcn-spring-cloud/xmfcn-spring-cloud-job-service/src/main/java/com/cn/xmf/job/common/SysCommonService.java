package com.cn.xmf.job.common;

import com.alibaba.fastjson.JSONObject;
import com.cn.xmf.base.model.RetCodeAndMessage;
import com.cn.xmf.base.model.RetData;
import com.cn.xmf.enums.DingMessageType;
import com.cn.xmf.job.kafka.IKafkaReader;
import com.cn.xmf.job.sys.DictService;
import com.cn.xmf.job.sys.KafKaProducerService;
import com.cn.xmf.job.sys.RedisService;
import com.cn.xmf.model.ding.DingMessage;
import com.cn.xmf.util.ConstantUtil;
import com.cn.xmf.util.StringUtil;
import com.cn.xmf.job.sys.DingTalkService;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.common.TopicPartition;
import org.redisson.api.RLock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author rufei.cn
 * <p>公共处理方法模块 $DESCRIPTION</p>
 */
@Service
@SuppressWarnings("all")
public class SysCommonService {

    private static Logger logger = LoggerFactory.getLogger(SysCommonService.class);
    public static Map<String, String> cacheMap = new HashMap<String, String>();//字典数据本机缓存，减少rpc 调用
    private static ExecutorService cachedThreadPool = Executors.newFixedThreadPool(200);//线程池
    @Autowired
    private DingTalkService dingTalkService;
    @Autowired
    private RedisService redisService;
    @Autowired
    private Environment environment;
    @Autowired
    private DictService dictService;
    @Autowired
    private KafKaProducerService kafKaProducerService;


    /**
     * 获取当前运行的系统名称
     *
     * @return
     */
    public String getSysName() {
        return environment.getProperty("spring.application.name");
    }

    /**
     * setDingMessage(组织钉钉消息)
     *
     * @param method
     * @param parms
     * @return
     */
    public void sendDingMessage(String method, String parms, String retData, String msg, Class t) {
        try {
            DingMessage dingMessage = new DingMessage();
            dingMessage.setDingMessageType(DingMessageType.MARKDWON);
            dingMessage.setSysName(getSysName());
            dingMessage.setModuleName(t.getPackage().toString());
            dingMessage.setMethodName(method);
            dingMessage.setParms(parms);
            dingMessage.setExceptionMessage(msg);
            dingMessage.setRetData(retData);
            dingTalkService.sendMessageToDingTalk(dingMessage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * save(保持缓存)
     *
     * @param key
     * @return
     */
    public void save(String key, String value, int seconds) {
        try {
            if (StringUtil.isBlank(key)) {
                return;
            }
            redisService.save(key, value, seconds);
        } catch (Exception e) {
            logger.error("save_error:" + StringUtil.getExceptionMsg(e));
            e.printStackTrace();
        }
    }

    /**
     * getCache(获取缓存)
     *
     * @param key
     * @return
     */
    public String getCache(String key) {
        String cache = null;
        if (StringUtil.isBlank(key)) {
            return null;
        }
        try {
            redisService.getCache(key);
        } catch (Exception e) {
            logger.error("getCache_error:" + StringUtil.getExceptionMsg(e));
            e.printStackTrace();
        }
        return cache;
    }

    /**
     * delete(删除缓存)
     *
     * @param key
     * @return
     */
    public long delete(String key) {
        long result = -1;
        try {
            if (StringUtil.isBlank(key)) {
                return result;
            }
            result = redisService.delete(key);
        } catch (Exception e) {
            logger.error("delete_error:" + StringUtil.getExceptionMsg(e));
            e.printStackTrace();
        }
        return result;
    }


    /**
     * getLock（获取分布式锁）
     *
     * @param key
     * @return
     * @author airuei
     */
    public RLock getLock(String key) {
        RLock lock = null;
        if (StringUtil.isBlank(key)) {
            return lock;
        }
        try {
            //lock = redisService.getLock(key);
        } catch (Exception e) {
            logger.error("getLock（获取分布式锁）:" + StringUtil.getExceptionMsg(e));
            e.printStackTrace();
        }
        return lock;
    }

    /**
     * getRedisInfo（redis 运行健康信息)
     *
     * @param key
     * @return
     */
    public JSONObject getRedisInfo() {
        JSONObject result = null;
        try {
            result = redisService.getRedisInfo();
        } catch (Exception e) {
            logger.error("getRedisInfo（redis 运行健康信息):" + StringUtil.getExceptionMsg(e));
            e.printStackTrace();
        }
        return result;
    }


    /**
     * 获取字典数据
     *
     * @param dictType
     * @param dictKey
     * @return
     */
    public String getDictValue(String dictType, String dictKey) {
        String dictValue = null;
        String key = ConstantUtil.CACHE_SYS_BASE_DATA_ + dictType + dictKey;
        try {
            dictValue = cacheMap.get(key);
            if (StringUtil.isNotBlank(dictValue)) {
                cachedThreadPool.execute(() -> {
                    cleanLoadCache(key);//定时清除缓存
                });
                dictValue = dictValue.replace("@0", "");
                return dictValue;
            }
            dictValue = dictService.getDictValue(dictType, dictKey);
            if (StringUtil.isBlank(dictValue)) {
                cacheMap.put(key, "@0");
            } else {
                cacheMap.put(key, dictValue);
            }
        } catch (Exception e) {
            logger.error(StringUtil.getExceptionMsg(e));
            e.printStackTrace();
        }
        return dictValue;
    }

    /**
     * 定时清除本地缓存
     *
     * @param key
     */
    private void cleanLoadCache(String key) {
        try {
            Thread.sleep(1000 * 60);
            cacheMap.remove(key);
            logger.info("清除本地缓存,key={}", key);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 处理失败后再次放入队列
     *
     * @param topic
     * @param value
     */
    public void retry(String topic, JSONObject json) {
        if (StringUtil.isBlank(topic)) {
            return;
        }
        if (json == null) {
            return;
        }
        int queueNum = json.getIntValue("queueNum");
        int retryIntoQueueNum = StringUtil.stringToInt(getDictValue(ConstantUtil.DICT_TYPE_BASE_CONFIG, "retry_into_queue_num"));
        if (retryIntoQueueNum < 0) {
            retryIntoQueueNum = 3;
        }
        if (queueNum > retryIntoQueueNum) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("topic:").append(topic).append("\n json:").append(json).append("\n msg:");
            stringBuilder.append("队列数据已经超过").append(retryIntoQueueNum);
            stringBuilder.append("次重试,需要人工处理");
            logger.info(stringBuilder.toString());
            sendDingMessage("retry", json.toString(), null, stringBuilder.toString(), this.getClass());
            return;
        }
        try {
            json.put("queueNum", queueNum + 1);
            sendKafka(topic, null, json.toString());
        } catch (Exception e) {
            String parms = "  参数：topic=" + topic + " key=" + topic + " value=" + json.toString();
            String exceptionMsg = "重入kafka队列失败：" + StringUtil.getExceptionMsg(e) + parms;
            logger.error(exceptionMsg);
            sendDingMessage("retry", parms, null, exceptionMsg, this.getClass());
            e.printStackTrace();
        }
    }

    /**
     * 根据处理队列数据返回结果执行是否重入队列、清除缓存等操作
     *
     * @param topic
     * @param json
     * @param retData
     */
    public void isRetryKafka(String topic, JSONObject json, RetData retData) {
        if (StringUtil.isBlank(topic)) {
            logger.info("topic 为空");
        }
        if (json == null) {
            logger.info("json 数据 为空");
        }
        if (retData == null) {
            String key = StringUtil.getUuId();
            retry(topic, json);//重入异常队列
            return;
        }
        int code = retData.getCode();
        if (code != RetCodeAndMessage.FAILURE) {
            return;
        }
        String value = json.getString("value");
        JSONObject retryJosn = JSONObject.parseObject(value);
        if (retryJosn == null) {
            logger.info("retryJosn 数据 为空");
            return;
        }
        String key = StringUtil.getUuId();
        retry(topic, json);//重入异常队列
    }

    /**
     * sendKafka（发送数据到kafka）
     *
     * @param topic
     * @param key
     * @param value
     * @return
     */
    public boolean sendKafka(String topic, String key, String value) {
        boolean result = false;
        if (StringUtil.isBlank(topic)) {
            logger.info("topic不能为空");
        }
        if (StringUtil.isBlank(value)) {
            logger.info("value不能为空");
        }
        JSONObject sendJson = new JSONObject();
        sendJson.put("topic", topic);
        sendJson.put("key", key);
        sendJson.put("value", value);
        try {
            result = kafKaProducerService.sendKafka(sendJson);
        } catch (Exception e) {
            logger.error("sendKafka（发送数据到kafka）:" + StringUtil.getExceptionMsg(e));
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 消费kafka队列数据
     *
     * @param kafkaConsumer 消费实例
     * @param topic         消费主题
     * @param kafkaReader   消费类实例
     * @param taskName      任务名称
     */
    public void readKafkaData(KafkaConsumer<String, String> kafkaConsumer, String topic, IKafkaReader kafkaReader, String taskName) {
        try {
            if (kafkaConsumer == null) {
                logger.info(taskName + "kafkaConsumer 消费者实例 为空");
                return;
            }
            if (StringUtil.isBlank(topic)) {
                logger.info(taskName + "topic 消费主题 为空");
                return;
            }
            kafkaConsumer.subscribe(Collections.singletonList(topic));
            while (true) {
                int randNum = 200;
                boolean isSleep = false;
                long startTime = System.currentTimeMillis();
                ConsumerRecords<String, String> records = null;
                try {
                    if (isSleep) {
                        randNum = StringUtil.getRandNum(500, 2000);
                        Thread.sleep(randNum);
                    }
                    records = kafkaConsumer.poll(500);
                } catch (Exception e) {
                    logger.error(taskName + " kafkaConsumer.poll" + StringUtil.getExceptionMsg(e));
                }
                StringBuilder stringBuilder = new StringBuilder();
                randNum = StringUtil.getRandNum(500, 3000);
                if (records == null) {
                    Thread.sleep(randNum);
                    continue;
                }
                Set<TopicPartition> partitions = records.partitions();
                if (partitions == null || partitions.size() <= 0) {
                    Thread.sleep(randNum);
                    continue;
                }
                for (TopicPartition partition : partitions) {
                    List<ConsumerRecord<String, String>> partitionRecords = records.records(partition);
                    if (partitionRecords == null || partitionRecords.size() <= 0) {
                        stringBuilder.append(taskName).append(" partitionRecords  没有队列数据");
                        logger.info(stringBuilder.toString());
                        Thread.sleep(randNum);
                        continue;
                    }
                    int len = partitionRecords.size();
                    ConsumerRecord<String, String> record = partitionRecords.get(0);
                    long newOffset = record.offset() + len;
                    cachedThreadPool.execute(() -> {
                        RetData aReturn = kafkaReader.executeList(partitionRecords, topic);
                    });
                    // 逐个异步提交消费成功，避免异常导致无法提交而造成重复消费
                    kafkaConsumer.commitAsync(Collections.singletonMap(partition, new OffsetAndMetadata(newOffset)), (map, e) -> {
                        if (e != null) {
                            logger.error(taskName + " 提交失败 offset={},e={}", record.offset(), e);
                        }
                    });
                }
            }
        } catch (InterruptedException e) {
            logger.error(taskName + StringUtil.getExceptionMsg(e));
            e.printStackTrace();
        } catch (Exception e) {
            logger.error(taskName + StringUtil.getExceptionMsg(e));
            e.printStackTrace();
        }
    }
}
