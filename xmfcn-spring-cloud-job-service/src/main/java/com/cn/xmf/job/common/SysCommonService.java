package com.cn.xmf.job.common;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.cn.xmf.base.Interface.SysCommon;
import com.cn.xmf.base.model.RetCodeAndMessage;
import com.cn.xmf.base.model.RetData;
import com.cn.xmf.enums.DingMessageType;
import com.cn.xmf.job.kafka.IKafkaReader;
import com.cn.xmf.job.sys.DictService;
import com.cn.xmf.job.sys.DingTalkService;
import com.cn.xmf.job.sys.KafKaProducerService;
import com.cn.xmf.job.sys.RedisService;
import com.cn.xmf.model.ding.DingMessage;
import com.cn.xmf.util.ConstantUtil;
import com.cn.xmf.util.LocalCacheUtil;
import com.cn.xmf.util.StringUtil;
import com.cn.xmf.util.TreadPoolUtil;
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

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author rufei.cn
 * <p>公共处理方法模块 $DESCRIPTION</p>
 */
@Service
@SuppressWarnings("all")
public class SysCommonService implements SysCommon {

    private static ThreadPoolExecutor cachedThreadPool = TreadPoolUtil.getCommonThreadPool();//获取公共线程池
    private static Logger logger = LoggerFactory.getLogger(SysCommonService.class);

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
    @Override
    public void sendDingMessage(String method, Object parms, Object retData, String msg, Class t) {
        try {
            DingMessage dingMessage = new DingMessage();
            dingMessage.setDingMessageType(DingMessageType.MARKDWON);
            dingMessage.setSysName(getSysName());
            dingMessage.setModuleName(t.getPackage().toString());
            dingMessage.setMethodName(method);
            if (parms != null) {
                dingMessage.setParms(parms.toString());
            }
            if (retData != null) {
                dingMessage.setRetData(retData.toString());
            }
            dingMessage.setExceptionMessage(msg);
            String classMethod = this.getClass().getName() + ".sendDingMessage()";
            TreadPoolUtil.getThreadPoolIsNext(cachedThreadPool, classMethod);
            cachedThreadPool.execute(() -> {
                dingTalkService.sendMessageToDingTalk(dingMessage);
            });
        } catch (Exception e) {
            logger.error(StringUtil.getExceptionMsg(e));
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
    @Override
    public String getDictValue(String dictType, String dictKey) {
        String dictValue = null;
        String key = ConstantUtil.CACHE_SYS_BASE_DATA_ + dictType + dictKey;
        try {
            dictValue = LocalCacheUtil.getCache(key);
            if (StringUtil.isNotBlank(dictValue)) {
                dictValue = dictValue.replace("@0", "");
                return dictValue;
            }
            dictValue = dictService.getDictValue(dictType, dictKey);
            if (StringUtil.isBlank(dictValue)) {
                LocalCacheUtil.saveCache(key, "@0", 60);
            } else {
                LocalCacheUtil.saveCache(key, dictValue, 60);
            }
        } catch (Exception e) {
            logger.error(StringUtil.getExceptionMsg(e));
        }
        return dictValue;
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
     * 根据处理队列数据返回结果执行是否重入队列、清除缓存等操作
     *
     * @param topic
     * @param json
     * @param dataReturn
     */
    public void isRetryKafka(String topic, RetData dataReturn) {
        if (StringUtil.isBlank(topic)) {
            logger.info("topic 为空");
        }
        if (dataReturn == null) {
            return;
        }
        int code = dataReturn.getCode();
        if (code != RetCodeAndMessage.FAILURE) {
            return;
        }
        String jsonString = JSON.toJSONString(dataReturn);
        logger.error("执行队列数据发生异常 topic={},dataReturn={}", topic, jsonString);
        if (StringUtil.isBlank(jsonString)) {
            return;
        }
        JSONObject jsonObject = JSONObject.parseObject(jsonString);
        if (jsonObject == null || jsonObject.size() <= 0) {
            return;
        }
        JSONArray jsonArray = jsonObject.getJSONArray("data");
        if (jsonArray == null || jsonArray.size() <= 0) {
            return;
        }
        int size = jsonArray.size();
        for (int i = 0; i < size; i++) {
            JSONObject object = jsonArray.getJSONObject(i);
            if (object == null) {
                return;
            }
            retry(topic, object);//重入异常队列
        }
    }

    /**
     * sendKafka（发送数据到kafka）
     *
     * @param topic
     * @param key
     * @param value
     * @return
     */
    @Override
    public boolean sendKafka(String topic, String key, Object value) {
        boolean result = false;
        if (StringUtil.isBlank(topic)) {
            logger.info("topic不能为空");
        }
        if (value == null) {
            logger.info("value不能为空");
        }
        if (kafKaProducerService == null) {
            return result;
        }
        JSONObject sendJson = new JSONObject();
        sendJson.put("topic", topic);
        sendJson.put("key", key);
        sendJson.put("value", value);
        try {
            result = kafKaProducerService.sendKafka(sendJson);
        } catch (Exception e) {
            logger.error("sendKafka（发送数据到kafka）:" + StringUtil.getExceptionMsg(e));
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
     * @param taskName      任务名称
     */
    public RetData readKafkaData(KafkaConsumer<String, String> kafkaConsumer, String topic, IKafkaReader kafkaReader, String taskName, boolean isList) {
        RetData dataReturn = new RetData();
        if (kafkaConsumer == null) {
            String msg = taskName + "kafkaConsumer 消费者实例 为空";
            logger.info(msg);
            dataReturn.setCode(RetCodeAndMessage.NO_DATA);
            return dataReturn;
        }
        if (StringUtil.isBlank(topic)) {
            String msg = taskName + "topic 消费主题 为空";
            logger.info(msg);
            dataReturn.setCode(RetCodeAndMessage.NO_DATA);
            dataReturn.setMessage(msg);
            return dataReturn;
        }
        kafkaConsumer.subscribe(Collections.singletonList(topic));
        boolean isNext = true;
        while (isNext) {
            boolean kafkaisReLoadConsmer = getKafkaisReLoadConsmer(topic);//判断是否热加载
            if (kafkaisReLoadConsmer) {
                dataReturn.setCode(RetCodeAndMessage.NO_DATA);
                dataReturn.setMessage("kafka实例需要重新加载");
                isNext = false;
                kafkaConsumer = null;
                continue;
            }
            ConsumerRecords<String, String> records = null;
            try {
                records = kafkaConsumer.poll(1000);
            } catch (Exception e) {
                logger.error(StringUtil.getExceptionMsg(e));
                e.printStackTrace();
            }
            StringBuilder stringBuilder = new StringBuilder();
            int randNum = StringUtil.getRandNum(500, 5000);
            if (records == null || records.isEmpty()) {
                StringUtil.threadSleep(randNum);
                continue;
            }
            Set<TopicPartition> partitions = records.partitions();
            if (partitions == null || partitions.size() <= 0) {
                StringUtil.threadSleep(randNum);
                continue;
            }
            for (TopicPartition partition : partitions) {
                List<ConsumerRecord<String, String>> partitionRecords = records.records(partition);
                if (partitionRecords == null || partitionRecords.size() <= 0) {
                    stringBuilder.append(taskName).append(" partitionRecords  没有队列数据");
                    logger.info(stringBuilder.toString());
                    StringUtil.threadSleep(randNum);
                    continue;
                }
                if (isList) {//处理集合形式的数据，集合形式尚未有重试功能
                    int len = partitionRecords.size();
                    ConsumerRecord<String, String> record = partitionRecords.get(0);
                    long newOffset = record.offset() + len;
                    String classMethod = this.getClass().getName() + ".readKafkaData()";
                    TreadPoolUtil.getThreadPoolIsNext(cachedThreadPool, classMethod);
                    cachedThreadPool.execute(() -> {
                        try {
                            RetData aReturn = kafkaReader.executeList(partitionRecords, topic);
                            isRetryKafka(topic, aReturn);
                        } catch (Exception e) {
                            logger.error("处理kafka集合数据异常：" + StringUtil.getExceptionMsg(e));
                            e.printStackTrace();
                        }
                    });
                    // 逐个异步提交消费成功，避免异常导致无法提交而造成重复消费
                    kafkaConsumer.commitAsync(Collections.singletonMap(partition, new OffsetAndMetadata(newOffset)), (map, e) -> {
                        if (e != null) {
                            logger.error(taskName + " 提交失败 offset={},e={}", record.offset(), e);
                        }
                    });
                }
                getPartitionRecords(kafkaConsumer, topic, kafkaReader, partition, partitionRecords);
            }
        }
        return dataReturn;
    }

    /**
     * 获取每个分区的数据
     *
     * @param kafkaConsumer
     * @param topic
     * @param kafkaReader
     * @param partition
     * @param partitionRecords
     */
    private void getPartitionRecords(KafkaConsumer<String, String> kafkaConsumer, String topic, IKafkaReader kafkaReader, TopicPartition partition, List<ConsumerRecord<String, String>> partitionRecords) {
        boolean isSleep;
        for (ConsumerRecord<String, String> record : partitionRecords) {
            String value = record.value();//数据
            if (StringUtil.isBlank(value)) {
                continue;
            }
            String key = record.key();
            long offset = record.offset();
            JSONObject json = new JSONObject();
            json.put("key", key);
            json.put("value", value);
            json.put("offset", offset);
            json.put("topic", topic);
            String classMethod = this.getClass().getName() + ".getPartitionRecords()";
            TreadPoolUtil.getThreadPoolIsNext(cachedThreadPool, classMethod);//判断激活的线程数量与最大线程的比列 如果大于80% 则暂停N秒
            cachedThreadPool.execute(() -> {
                try {
                    RetData aReturn = kafkaReader.execute(json);
                    isRetryKafka(topic, json, aReturn);
                } catch (Exception e) {
                    logger.error("处理kafka数据异常：" + StringUtil.getExceptionMsg(e) + "===>原始数据：" + json);
                    e.printStackTrace();
                }
            });
            // 逐个异步提交消费成功，避免异常导致无法提交而造成重复消费
            kafkaConsumer.commitAsync(Collections.singletonMap(partition, new OffsetAndMetadata(record.offset() + 1)), (map, e) -> {
                if (e != null) {
                    logger.error(" 提交失败 offset={},e={}", record.offset(), e);
                }
            });
        }
    }

    /**
     * 根据主题获取kafka消费实例是否重新加载
     *
     * @param topic
     * @return
     */
    public boolean getKafkaisReLoadConsmer(String topic) {
        boolean result = false;
        if (StringUtil.isBlank(topic)) {
            return result;
        }
        String key = ConstantUtil.CACHE_SYS_BASE_DATA_ + "getKafkaisReLoadConsmer" + topic;
        String redisCache = getCache(key);
        if (StringUtil.isNotBlank(redisCache)) {
            return result;
        }
        String dictValue = getDictValue(ConstantUtil.DICT_TYPE_CONFIG_KAFKA, topic);
        JSONObject jsonObject = null;
        try {
            jsonObject = JSONObject.parseObject(dictValue);
        } catch (Exception e) {
            logger.error(StringUtil.getExceptionMsg(e));
        }
        if (jsonObject == null) {
            return result;
        }
        result = jsonObject.getBooleanValue("isReloadKafka");
        if (result) {
            save(key, "false", 60 * 5);//五分钟内无需对同一个topic实例进行实例加载
        }
        return result;
    }

}
