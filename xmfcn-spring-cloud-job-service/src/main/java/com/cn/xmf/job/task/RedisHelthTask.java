package com.cn.xmf.job.task;

import com.alibaba.fastjson.JSONObject;
import com.cn.xmf.base.model.ResultCodeMessage;
import com.cn.xmf.job.common.SysCommonService;
import com.cn.xmf.job.core.biz.model.ReturnT;
import com.cn.xmf.job.core.handler.IJobHandler;
import com.cn.xmf.job.core.handler.annotation.JobHandler;
import com.cn.xmf.job.core.log.XxlJobLogger;
import com.cn.xmf.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


/**
 * 任务Handler示例（Bean模式）获取redis的运行信息任务
 * <p>
 * 开发步骤：
 * 1、继承"IJobHandler"：“com.cn.xmf.job.core.handler.IJobHandler”；
 * 2、注册到Spring容器：添加“@Component”注解，被Spring容器扫描为Bean实例；
 * 3、注册到执行器工厂：添加“@JobHandler(value="自定义jobhandler名称")”注解，注解value值对应的是调度中心新建任务的JobHandler属性的值。
 * 4、执行日志：需要通过 "XxlJobLogger.user" 打印执行日志；
 *
 * @author xuxueli 2015-12-19 19:43:36
 */
@JobHandler(value = "redisHelthTask")
@Component
@SuppressWarnings("all")
public class RedisHelthTask extends IJobHandler {
    private static Logger logger = LoggerFactory.getLogger(RedisHelthTask.class);
    @Autowired
    private SysCommonService sysCommonService;

    /**
     * 获取redis的运行信息任务
     *
     * @param param
     * @return
     */
    @Override
    public ReturnT<String> execute(String param) {
        ReturnT<String> retData = new ReturnT<>(ResultCodeMessage.FAILURE, ResultCodeMessage.FAILURE_MESSAGE);
        try {
            JSONObject redisInfo = sysCommonService.getRedisInfo();
            JSONObject jsonObject = null;
            if (redisInfo != null) {
                jsonObject = redisInfo.getJSONObject("info");
            }
            if (jsonObject != null) {
                sysCommonService.sendDingTalkMessage("execute()", param, null, jsonObject.toString(), this.getClass());
                retData.setCode(ResultCodeMessage.SUCCESS);
                retData.setMsg(ResultCodeMessage.SUCCESS_MESSAGE);
                XxlJobLogger.log("获取redis的运行信息任务", ResultCodeMessage.SUCCESS_MESSAGE);
            }
        } catch (Exception e) {
            retData.setMsg(ResultCodeMessage.EXCEPTION_MESSAGE);
            logger.error("获取redis的运行信息任务 异常={}", StringUtil.getExceptionMsg(e));
            XxlJobLogger.log("获取redis的运行信息任务 异常={}", StringUtil.getExceptionMsg(e));
        }
        return retData;
    }
}
