package com.cn.xmf.config;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.spi.LoggingEvent;
import ch.qos.logback.core.AppenderBase;
import com.alibaba.fastjson.JSONObject;
import com.cn.xmf.base.Interface.SysCommon;
import com.cn.xmf.util.ConstantUtil;
import com.cn.xmf.util.SpringUtil;
import com.cn.xmf.util.StringUtil;
import com.cn.xmf.util.TreadPoolUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;
import java.util.concurrent.ThreadPoolExecutor;

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
    public void start(LoggingEvent loggingEvent) {
        try {
            SysCommon sysCommonService = null;
            try {
                sysCommonService = (SysCommon) SpringUtil.getBean("sysCommonService");
            } catch (Exception e) {
            }
            if (sysCommonService == null) {
                return;
            }
            String loggerName = loggingEvent.getLoggerName();
            String message = loggingEvent.getMessage();
            Level level = loggingEvent.getLevel();
            if (message == null) {
                return;
            }
            if (loggerName == null) {
                return;
            }
            if (!loggerName.startsWith("com.cn.xmf") && level == Level.INFO) {
                return;
            }
            String log_type = "log_filter_keyword";//需要过滤的日志信息
            boolean filterLog = filterLog(message, log_type);
            if (filterLog) {//过滤不需要的日志信息
                return;
            }
            String jsonString = StringUtil.getLogData(loggingEvent, subSysName);
            JSONObject jsonObject = null;
            try {
                jsonObject = JSONObject.parseObject(jsonString);
            } catch (Exception e) {
                logger.error(StringUtil.getExceptionMsg(e));
            }
            if (jsonObject == null || jsonObject.size() <= 0) {
                return;
            }
            sysCommonService.sendKafka(topic, null, jsonObject.toString());
            // 方法是异步的，添加消息到缓冲区等待发送，并立即返回。生产者将单个的消息批量在一起发送来提高效率。
        } catch (Exception e) {
            logger.error(StringUtil.getExceptionMsg(e));
        }
    }

    //过滤不需要的日志信息
    private boolean filterLog(String message, String log_type) {
        boolean isFilterLog = false;//不过滤
        if (StringUtil.isBlank(message)) {
            return isFilterLog;
        }
        SysCommon sysCommonService = null;
        try {
            sysCommonService = (SysCommon) SpringUtil.getBean("sysCommonService");
        } catch (Exception e) {
        }
        if (sysCommonService == null) {
            return isFilterLog;
        }
        String logFilterLogWords = sysCommonService.getDictValue(log_type, "log_filter_log_words");
        if(StringUtil.isBlank(logFilterLogWords))
        {
            return isFilterLog;
        }
        String[] split = logFilterLogWords.split(",");
        if (split == null || split.length <= 0) {
            return isFilterLog;
        }
        for (String item : split) {
            if (message.contains(item)) {
                isFilterLog = true;//需要过滤
                break;
            }
        }
        return isFilterLog;
    }

    @Override
    protected void append(LoggingEvent loggingEvent) {
        String classMethod=this.getClass().getName()+".append()";
        TreadPoolUtil.getThreadPoolIsNext(cachedThreadPool,classMethod);
        cachedThreadPool.execute(() -> start(loggingEvent));//异步执行
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
