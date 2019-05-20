package com.cn.xmf.job.kafka.es;

import com.alibaba.fastjson.JSONObject;
import com.cn.xmf.base.model.RetCodeAndMessage;
import com.cn.xmf.base.model.RetData;
import com.cn.xmf.job.kafka.IKafkaReader;
import com.cn.xmf.job.common.SysCommonService;
import com.cn.xmf.job.sys.ElasticsearchService;
import com.cn.xmf.model.sys.EsModel;
import com.cn.xmf.util.ConstantUtil;
import com.cn.xmf.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * kafka数据写入ES系统存储任务
 *  @author rufei.cn
 *  @version 2019-05-19
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
     * @param jsonObject
     * @return
     */
    @Override
    public RetData execute(JSONObject jsonObject) {
        RetData dataReturn = new RetData();
        dataReturn.setCode(RetCodeAndMessage.DATA_ERROR);
        dataReturn.setMessage(RetCodeAndMessage.SUCCESS_MESSAGE);
        if (jsonObject == null) {
            return dataReturn;
        }
        String key = jsonObject.getString("key");
        String value = jsonObject.getString("value");
        String offset = jsonObject.getString("offset");
        try {
            EsModel es = new EsModel();
            es.setIndex(ConstantUtil.ES_SYS_LOG_INDEX);
            es.setType(ConstantUtil.ES_SYS_LOG_TYPE);
            es.setMessage(value);
            dataReturn = elasticsearchService.save(es);//kafka数据写入ES系统存储任务
        } catch (Exception e) {
            String msg = "【kafka数据写入ES系统存储任务】" + StringUtil.getExceptionMsg(e);
            dataReturn.setCode(RetCodeAndMessage.SYS_ERROR);
            dataReturn.setMessage(msg);
            logger.error(msg);
            e.printStackTrace();
        }
        if (dataReturn == null) {
            dataReturn = new RetData();
            dataReturn.setCode(RetCodeAndMessage.SYS_ERROR);
            dataReturn.setMessage(RetCodeAndMessage.SYS_ERROR_MESSAGE);
        }
        return dataReturn;
    }
}
