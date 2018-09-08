package com.cn.xmf.job.admin.core.route.strategy;

import com.cn.xmf.job.admin.core.schedule.XxlJobDynamicScheduler;
import com.cn.xmf.job.admin.core.trigger.XxlJobTrigger;
import com.cn.xmf.job.admin.core.route.ExecutorRouter;
import com.cn.xmf.job.admin.core.schedule.XxlJobDynamicScheduler;
import com.cn.xmf.job.admin.core.trigger.XxlJobTrigger;
import com.cn.xmf.job.admin.core.util.I18nUtil;
import com.cn.xmf.job.core.biz.ExecutorBiz;
import com.cn.xmf.job.core.biz.model.ReturnT;
import com.cn.xmf.job.core.biz.model.TriggerParam;

import java.util.ArrayList;

/**
 * Created by xuxueli on 17/3/10.
 */
public class ExecutorRouteBusyover extends ExecutorRouter {

    public String route(int jobId, ArrayList<String> addressList) {
        return addressList.get(0);
    }

    @Override
    public ReturnT<String> routeRun(TriggerParam triggerParam, ArrayList<String> addressList) {

        StringBuffer idleBeatResultSB = new StringBuffer();
        for (String address : addressList) {
            // beat
            ReturnT<String> idleBeatResult = null;
            try {
                ExecutorBiz executorBiz = XxlJobDynamicScheduler.getExecutorBiz(address);
                idleBeatResult = executorBiz.idleBeat(triggerParam.getJobId());
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
                idleBeatResult = new ReturnT<String>(ReturnT.FAIL_CODE, ""+e );
            }
            idleBeatResultSB.append( (idleBeatResultSB.length()>0)?"<br><br>":"")
                    .append(I18nUtil.getString("jobconf_idleBeat") + "：")
                    .append("<br>address：").append(address)
                    .append("<br>code：").append(idleBeatResult.getCode())
                    .append("<br>msg：").append(idleBeatResult.getMsg());

            // beat success
            if (idleBeatResult.getCode() == ReturnT.SUCCESS_CODE) {

                ReturnT<String> runResult = XxlJobTrigger.runExecutor(triggerParam, address);
                idleBeatResultSB.append("<br><br>").append(runResult.getMsg());

                // result
                runResult.setMsg(idleBeatResultSB.toString());
                runResult.setContent(address);
                return runResult;
            }
        }

        return new ReturnT<String>(ReturnT.FAIL_CODE, idleBeatResultSB.toString());
    }
}
