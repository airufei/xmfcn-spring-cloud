package com.cn.xmf.job.core.biz;

import com.cn.xmf.job.core.biz.model.LogResult;
import com.cn.xmf.job.core.biz.model.ReturnT;
import com.cn.xmf.job.core.biz.model.TriggerParam;

/**
 * 执行器API服务
 * Created by xuxueli on 17/3/1.
 */
public interface ExecutorBiz {

    /**
     * beat（心跳检测）
     *
     * @return
     */
    public ReturnT<String> beat();

    /**
     * idle beat（忙碌检测）
     *
     * @param jobId
     * @return
     */
    public ReturnT<String> idleBeat(int jobId);

    /**
     * kill（终止任务）
     *
     * @param jobId
     * @return
     */
    public ReturnT<String> kill(int jobId);

    /**
     * user（获取Rolling Log）
     *
     * @param logDateTim
     * @param logId
     * @param fromLineNum
     * @return
     */
    public ReturnT<LogResult> log(long logDateTim, int logId, int fromLineNum);

    /**
     * run（触发任务执行）
     *
     * @param triggerParam
     * @return
     */
    public ReturnT<String> run(TriggerParam triggerParam);

}
