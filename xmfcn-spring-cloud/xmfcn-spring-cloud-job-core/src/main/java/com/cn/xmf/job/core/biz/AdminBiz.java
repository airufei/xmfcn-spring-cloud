package com.cn.xmf.job.core.biz;

import com.cn.xmf.job.core.biz.model.HandleCallbackParam;
import com.cn.xmf.job.core.biz.model.RegistryParam;
import com.cn.xmf.job.core.biz.model.ReturnT;
import com.cn.xmf.job.core.biz.model.HandleCallbackParam;
import com.cn.xmf.job.core.biz.model.RegistryParam;
import com.cn.xmf.job.core.biz.model.ReturnT;

import java.util.List;

/**
 * 调度中心API服务
 *
 * @author xuxueli 2017-07-27 21:52:49
 */
public interface AdminBiz {

    public static final String MAPPING = "/api";


    // ---------------------- callback ----------------------

    /**
     * callback（任务结果回调服务）
     *
     * @param callbackParamList
     * @return
     */
    public ReturnT<String> callback(List<HandleCallbackParam> callbackParamList);


    // ---------------------- registry ----------------------

    /**
     * registry（执行器注册服务）
     *
     * @param registryParam
     * @return
     */
    public ReturnT<String> registry(RegistryParam registryParam);

    /**
     * registry remove（执行器注册摘除服务）
     *
     * @param registryParam
     * @return
     */
    public ReturnT<String> registryRemove(RegistryParam registryParam);


    // ---------------------- job opt ----------------------

    /**
     * trigger job for once（触发任务单次执行服务，支持任务根据业务事件触发）
     *
     * @param jobId
     * @return
     */
    public ReturnT<String> triggerJob(int jobId);

}
