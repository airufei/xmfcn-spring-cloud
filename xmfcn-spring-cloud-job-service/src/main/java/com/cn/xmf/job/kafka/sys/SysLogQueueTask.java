package com.cn.xmf.job.kafka.sys;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cn.xmf.base.model.RetCodeAndMessage;
import com.cn.xmf.base.model.RetData;
import com.cn.xmf.job.common.SysCommonService;
import com.cn.xmf.job.config.KafkaBean;
import com.cn.xmf.util.ConstantUtil;
import com.cn.xmf.util.StringUtil;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import javax.annotation.PostConstruct;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import com.cn.xmf.job.kafka.es.*;

/**
 * kafka数据写入ES系统存储任务
 */
@Component
@SuppressWarnings("all")
public class SysLogQueueTask {
    private static Logger logger = LoggerFactory.getLogger(SysLogQueueTask.class);
    private static String topic = ConstantUtil.XMF_KAFKA_TOPIC_LOG;
    @Autowired
    private SysCommonService sysCommonService;
    @Autowired
    private KafkaBean kafkaBean;
    private KafkaConsumer<String, String> kafkaConsumer = null;
    @Autowired
    private ElasticsearchServiceImpl elasticsearchServiceImpl;

    @PostConstruct
    public void init() {
        kafkaConsumer = kafkaBean.getKafkaConsumer(topic);//启动项目 产生一个消费者实例
        int randNum = StringUtil.getRandNum(30000, 60000);
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            public void run() {
                startTask();
                timer.cancel();
            }
        }, randNum);
    }

    /**
     * kafka数据写入ES系统存储任务
     *
     * @param parms
     * @return
     */
    public void startTask() {
        JSONObject jsonObject = null;
        try {
            logger.info("开始执行【kafka数据写入ES系统存储任务】");
            boolean isList = true;
            RetData dataReturn = sysCommonService.readKafkaData(kafkaConsumer, topic, elasticsearchServiceImpl, "sysLogQueueTask", isList);
            logger.info("【kafka数据写入ES系统存储任务】结束 " + JSON.toJSONString(dataReturn));
            if (dataReturn == null) {
                return ;
            }
            int code = dataReturn.getCode();
            if (code == RetCodeAndMessage.NO_DATA) {
                logger.info("准备重建消费实例 topic={}", topic);
                kafkaConsumer = kafkaBean.getKafkaConsumer(topic);//产生一个消费者实例
                startTask();
            }
        } catch (Exception e) {
            String exceptionMsg = "sysLogQueueTask" + StringUtil.getExceptionMsg(e);
            logger.error(exceptionMsg);
            sysCommonService.sendDingMessage("sysLogQueueTask", jsonObject.toString(), null, exceptionMsg, this.getClass());

        }
    }

}
