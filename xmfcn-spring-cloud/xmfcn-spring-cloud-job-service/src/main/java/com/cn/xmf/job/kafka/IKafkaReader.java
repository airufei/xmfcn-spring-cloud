package com.cn.xmf.job.kafka;

import com.alibaba.fastjson.JSONObject;
import com.cn.xmf.base.model.RetData;
import org.apache.kafka.clients.consumer.ConsumerRecord;

import java.util.List;

/**
 * kafka消费数据接口
 *
 * @author rufei.cn
 * @version 2019-05-19
 */
public interface IKafkaReader {

    /**
     * 获取kafka数据，执行业务操作
     *
     * @param partitionRecords 数据集
     * @param topic            主题
     * @return
     */
    public RetData execute(List<ConsumerRecord<String, String>> partitionRecords, String topic);
}
