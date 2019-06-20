package com.cn.xmf.job.task;

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

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


/**
 * 任务Handler示例（Bean模式）
 * <p>
 * 开发步骤：
 * 1、继承"IJobHandler"：“com.cn.xmf.job.core.handler.IJobHandler”；
 * 2、注册到Spring容器：添加“@Component”注解，被Spring容器扫描为Bean实例；
 * 3、注册到执行器工厂：添加“@JobHandler(value="自定义jobhandler名称")”注解，注解value值对应的是调度中心新建任务的JobHandler属性的值。
 * 4、执行日志：需要通过 "XxlJobLogger.user" 打印执行日志；
 *
 * @author xuxueli 2015-12-19 19:43:36
 */
@JobHandler(value = "rPopQueueTask")
@Component
public class RpopQueueTask extends IJobHandler {
    private static Logger logger = LoggerFactory.getLogger(RpopQueueTask.class);

    @Autowired
    private SysCommonService sysCommonService;

    @Override
    public ReturnT<String> execute(String param) throws Exception {
        return ReturnT.SUCCESS;
    }
}
