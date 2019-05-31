package com.cn.xmf.job.admin.sys.controller;

import com.alibaba.fastjson.JSONObject;
import com.cn.xmf.job.admin.common.SysCommonService;
import com.cn.xmf.job.admin.es.LogSearchHelperService;
import com.cn.xmf.job.admin.sys.ElasticsearchService;
import com.cn.xmf.job.core.biz.model.ReturnT;
import com.cn.xmf.model.es.EsModel;
import com.cn.xmf.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * LogSearchController(系统日志)
 *
 * @author airufei
 */
@Controller
@RequestMapping("/logcharts")
@SuppressWarnings("all")
public class LogChartsController {
    private static Logger logger = LoggerFactory.getLogger(LogSearchController.class);
    @Autowired
    private ElasticsearchService elasticsearchService;
    @Autowired
    private SysCommonService sysCommonService;
    @Autowired
    private LogSearchHelperService logSearchHelperService;

    @RequestMapping
    public String index(Model model) {
        return "es/log-charts-index";
    }

    /**
     * getLogLevelCharts(统计日志数据每个时间段 各个日志级别的数量)
     *
     * @param request
     * @return
     * @Author airufei
     */
    @RequestMapping("getLogLevelCharts")
    @ResponseBody
    public ReturnT<JSONObject> getStatisticsLevelNumByTime(HttpServletRequest request) {
        ReturnT<JSONObject> resJson = null;
        try {
            JSONObject resultJson = new JSONObject();
            EsModel parms = logSearchHelperService.getStatisticsParms(request);
            JSONObject levelJson = elasticsearchService.getStatisticsCountByLevel(parms);
            JSONObject dayJson = elasticsearchService.getStatisticsCountByDay(parms);
            if (levelJson != null) {
                resultJson.putAll(levelJson);
            }
            if (dayJson != null) {
                resultJson.putAll(dayJson);
            }
            logger.info(resultJson.toString());
            return new ReturnT<JSONObject>(resultJson);
        } catch (Exception e) {
            String msg = "getLogLevelCharts(统计日志数据每个时间段 各个日志级别的数量) 异常====>" + StringUtil.getExceptionMsg(e);
            logger.error(msg);
            e.printStackTrace();
        }
        return resJson;
    }
}
