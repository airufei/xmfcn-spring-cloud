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
import com.cn.xmf.util.StringUtil;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.aspectj.apache.bcel.generic.RET;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

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
     * @return
     * @Author airufei
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
        logger.info("【 kafka数据写入ES系统存储任务】 已经读出数据 topic={}, listCount={}", topic, partitionRecords.size());
        List<JSONObject> list = new ArrayList<>();
        int logListSize = getLogListSize();//每次入ES的集合数量，太大可能导致转json内存溢出。
        for (ConsumerRecord<String, String> record : partitionRecords) {
            String value = record.value();//数据
            String key = record.key();
            long offset = record.offset();
            if (StringUtil.isBlank(value)) {
                continue;
            }
            if (StringUtil.isBlank(value)) {
                continue;
            }
            boolean filterLog = isFilterLog(value);
            if (filterLog)//是否过滤包含某一些关键词的日志信息
            {
                continue;
            }
            JSONObject json = null;
            try {
                json = JSONObject.parseObject(value);
            } catch (Exception e) {
                logger.info("value={},errorMeg={}", value, StringUtil.getExceptionMsg(e));
            }
            if (json == null||json.size()<=0) {
                continue;
            }
            list.add(json);
            int size = list.size();
            if (size % logListSize == 0) {
                getDataReturn(retData, list);
                list = new ArrayList<>();
            }
        }
        retData = getDataReturn(retData, list);
        return retData;
    }

    private RetData getDataReturn(RetData dataReturn, List<JSONObject> list) {
        try {
            if (list == null || list.size() <= 0) {
                dataReturn.setMessage("没有数据");
                return dataReturn;
            }
            EsModel es = new EsModel();
            es.setIndex(ConstantUtil.ES_SYS_LOG_INDEX);
            es.setType(ConstantUtil.ES_SYS_LOG_TYPE);
            es.setList(list);
            dataReturn = elasticsearchService.saveBatch(es);//kafka数据写入ES系统存储任务
        } catch (Exception e) {
            String exceptionMsg = StringUtil.getExceptionMsg(e);
            logger.error(exceptionMsg);

        }
        return dataReturn;
    }

    /**
     * 每次入ES的集合数量，太大可能导致转json内存溢出。
     *
     * @return
     */
    public int getLogListSize() {
        int num = 1;
        String dictValue = sysCommonService.getDictValue(ConstantUtil.DICT_TYPE_BASE_CONFIG, "log_list_num");
        num = StringUtil.stringToInt(dictValue);
        if (num < 1) {
            num = 10;
        }
        return num;
    }

    /**
     * 是否过滤包含某一些关键词的日志信息
     *
     * @param message
     * @return
     */
    public boolean isFilterLog(String message) {
        boolean result = false;
        if (StringUtil.isBlank(message)) {
            return true;
        }
        String dictValue = sysCommonService.getDictValue(ConstantUtil.DICT_TYPE_BASE_CONFIG, "log_filter_words");//需要过滤的日志信息
        if (StringUtil.isBlank(dictValue)) {
            return result;
        }
        String[] array = dictValue.split(",");
        int len = 0;
        if (array == null || (len = array.length) <= 0) {
            array = dictValue.split("，");
        }
        if (array == null || (len = array.length) <= 0) {
            return result;
        }
        for (int i = 0; i < len; i++) {
            String item = array[i];
            if (StringUtil.isBlank(item)) {
                continue;
            }
            if (message.contains(item)) {
                result = true;
                break;
            }
        }
        return result;
    }
    /**
     * 获取kafka数据，执行业务操作
     *
     * @param jsonObject
     * @return
     * @Author airufei
     */
    @Override
    public RetData execute(JSONObject jsonObject) {
        return null;
    }
}
