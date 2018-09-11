package com.cn.xmf.config;

import ch.qos.logback.classic.spi.LoggingEvent;
import ch.qos.logback.core.AppenderBase;
import com.alibaba.fastjson.JSONObject;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class XmfLogAppender extends AppenderBase<LoggingEvent> {
    private static ExecutorService cachedThreadPool = Executors.newFixedThreadPool(3000);//线程池

    @Override
    protected void append(LoggingEvent loggingEvent) {
        try {
           /* String jsonString = StringUtil.getEsLogData(loggingEvent, subSysName);
            JSONObject map = new JSONObject();
            map.put("parms", jsonString);
            map.put("index", "sys_log_index");
            map.put("type", "t_sys_log");*/
            //writeLog(map);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void writeLog(final JSONObject json) {
        cachedThreadPool.execute(new Runnable() {//调用线程池线程执行回调数据
            @Override
            public void run() {
               // String url = "http://127.0.0.1:8082/eslog/log/saveLog";
               // HttpClientUtil.HttpsPost(url, json);
            }
        });
    }

    private String subSysName;

    public String getSubSysName() {
        return subSysName;
    }

    public void setSubSysName(String subSysName) {
        this.subSysName = subSysName;
    }
}
