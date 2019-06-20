package com.cn.xmf.job.admin.sys.controller;

import com.alibaba.fastjson.JSONObject;
import com.cn.xmf.job.admin.common.SysCommonService;
import com.cn.xmf.job.admin.es.LogSearchHelperService;
import com.cn.xmf.job.admin.sys.ElasticsearchService;
import com.cn.xmf.job.core.biz.model.ReturnT;
import com.cn.xmf.model.es.EsModel;
import com.cn.xmf.util.ConstantUtil;
import com.cn.xmf.util.HttpClientUtil;
import com.cn.xmf.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * LogSearchController(系统日志)
 *
 * @author airufei
 */
@Controller
@RequestMapping("/logcharts")
@SuppressWarnings("all")
public class LogChartsController {

    private static Logger logger = LoggerFactory.getLogger(LogChartsController.class);
    @Autowired
    private ElasticsearchService elasticsearchService;
    @Autowired
    private LogSearchHelperService logSearchHelperService;
    @Autowired
    private SysCommonService commonService;

    @RequestMapping
    public String index(Model model) {
        JSONObject sysInfo = getSysInfo();
        model.addAttribute("sysInfo", sysInfo);
        return "es/log-charts-index";
    }

    /**
     * 获取磁盘信息和内存信息
     *
     * @return
     */
    public JSONObject getSysInfo() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("diskused", 0);
        jsonObject.put("ramcurrent", 0);
        String url = "";
        String es_http_rest_url = commonService.getDictValue(ConstantUtil.DICT_TYPE_BASE_CONFIG, "es_http_rest_url");//获取ES地址 包括端口号
        if (StringUtil.isBlank(es_http_rest_url)) {
            return jsonObject;
        }
        es_http_rest_url = es_http_rest_url + "/_cat/nodes?v&h=disk.total,disk.used,ram.current,";
        String httpGet = null;
        try {
            httpGet = HttpClientUtil.httpsGet(es_http_rest_url);
        } catch (Exception e) {
            e.printStackTrace();
        }
        String[] split = null;
        if (StringUtil.isNotBlank(httpGet)) {
            split = httpGet.split("\n");
        }
        String s = null;
        if (split != null && split.length > 1) {
            s = split[1];
        }
        split = null;
        if (StringUtil.isNotBlank(s)) {
            split = s.trim().split(" ");
        }
        List<String> list = new ArrayList<>();
        if (split == null || split.length <= 0) {
            return jsonObject;
        }
        for (int i = 0; i < split.length; i++) {
            if (StringUtil.isNotBlank(split[i])) {
                list.add(split[i].trim());
            }
        }
        int size = list.size();
        if (size < 3) {
            return jsonObject;
        }
        jsonObject = new JSONObject();
        jsonObject.put("disktotal", list.get(0));
        jsonObject.put("diskused", list.get(1));
        jsonObject.put("ramcurrent", list.get(2));
        logger.info(jsonObject.toString());
        return jsonObject;
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
