package com.cn.xmf.job.kafka.es;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cn.xmf.base.model.RetCodeAndMessage;
import com.cn.xmf.base.model.RetData;
import com.cn.xmf.job.kafka.IKafkaReader;
import com.cn.xmf.job.common.SysCommonService;
import com.cn.xmf.job.sys.ElasticsearchService;
import com.cn.xmf.model.es.EsModel;
import com.cn.xmf.util.ConstantUtil;
import com.cn.xmf.util.DateUtil;
import com.cn.xmf.util.StringUtil;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.redisson.api.RLock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * kafka数据写入ES系统存储任务
 *
 * @author rufei.cn
 * @version 2019-05-19
 */
@SuppressWarnings("all")
@Service
public class ElasticsearchServiceImpl implements IKafkaReader {
    private static final Logger logger = LoggerFactory.getLogger(ElasticsearchServiceImpl.class);
    @Autowired
    private SysCommonService sysCommonService;
    @Autowired
    private ElasticsearchService elasticsearchService;

    /**
     * 获取kafka数据，执行业务操作
     *
     * @param partitionRecords 数据集
     * @param topic            主题
     * @Author airufei
     * @return
     */
    @Override
    public RetData executeList(List<ConsumerRecord<String, String>> partitionRecords, String topic) {
        RetData retData = new RetData();
        retData.setCode(RetCodeAndMessage.DATA_ERROR);
        retData.setMessage(RetCodeAndMessage.SUCCESS_MESSAGE);
        if (partitionRecords == null || partitionRecords.size() <= 0) {
            return retData;
        }
        if (StringUtil.isBlank(topic)) {
            return retData;
        }
        List<JSONObject> list = new ArrayList<>();
        for (ConsumerRecord<String, String> record : partitionRecords) {
            String value = record.value();//数据
            String key = record.key();
            long offset = record.offset();
            JSONObject json = JSONObject.parseObject(value);
            list.add(json);
        }
        try {
            int size = list.size();
            String jsonString = JSON.toJSONString(list);
            int length = jsonString.length();
            logger.info("  topic=" + topic + " ;size = " + size + " ;length = " + length);
            EsModel es = new EsModel();
            es.setIndex(ConstantUtil.ES_SYS_LOG_INDEX);
            es.setType(ConstantUtil.ES_SYS_LOG_TYPE);
            es.setMessage(JSON.toJSONString(list));
            retData = elasticsearchService.saveBatch(es);//kafka数据写入ES系统存储任务
        } catch (Exception e) {
            String exceptionMsg = StringUtil.getExceptionMsg(e);
            retData.setCode(RetCodeAndMessage.SYS_ERROR);
            retData.setMessage(exceptionMsg);
            logger.error(exceptionMsg);
            e.printStackTrace();
        }
        return retData;
    }


    /**
     * 获取kafka数据，执行业务操作
     *
     * @param jsonObject
     * @Author airufei
     * @return
     */
    @Override
    public RetData execute(JSONObject jsonObject) {
        return null;
    }
}
