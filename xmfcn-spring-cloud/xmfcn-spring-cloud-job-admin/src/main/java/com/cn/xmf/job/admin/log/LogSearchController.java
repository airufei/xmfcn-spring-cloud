package com.cn.xmf.job.admin.log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cn.xmf.job.admin.common.SysCommonService;
import com.cn.xmf.job.admin.sys.ElasticsearchService;
import com.cn.xmf.model.sys.EsModel;
import com.cn.xmf.util.ConstantUtil;
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
@RequestMapping("/log")
@SuppressWarnings("all")
public class LogSearchController {

    private static Logger logger = LoggerFactory.getLogger(LogSearchController.class);
    @Autowired
    private ElasticsearchService elasticsearchService;
    @Autowired
    private SysCommonService sysCommonService;

    @RequestMapping
    public String index(Model model) {
        List<JSONObject> list = new ArrayList();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("subSysName", "user-api");
        list.add(jsonObject);
        jsonObject = new JSONObject();
        jsonObject.put("subSysName", "base-service");
        list.add(jsonObject);
        jsonObject = new JSONObject();
        jsonObject.put("subSysName", "sys-service");
        list.add(jsonObject);
        jsonObject = new JSONObject();
        jsonObject.put("subSysName", "job-service");
        list.add(jsonObject);
        model.addAttribute("sysList", list);
        return "log/log-index";
    }

    /**
     * search:(系统日志搜索)
     *
     * @param request
     * @return
     * @Author airufei
     */
    @RequestMapping("search")
    @ResponseBody
    public JSONObject search(HttpServletRequest request) {
        JSONObject retJon = new JSONObject();
        JSONObject param = null;
        try {
            String subSysName = request.getParameter("subSysName");
            String message = request.getParameter("message");
            String traceId = request.getParameter("traceId");
            String stackMessage = request.getParameter("stackMessage");
            String methodName = request.getParameter("methodName");
            String level = request.getParameter("level");

            JSONObject parms = new JSONObject();
            List<String> list = new ArrayList<>();
            list.add("level");
            list.add("subSysName");
            JSONObject searchKey = new JSONObject();
            searchKey.put("subSysName", subSysName);
            searchKey.put("message", message);
            searchKey.put("traceId", traceId);
            searchKey.put("stackMessage", stackMessage);
            searchKey.put("methodName", methodName);
            searchKey.put("level", level);
            JSONObject keywords = new JSONObject();
            parms.put("keywords", searchKey);
            parms.put("highlights", list);
            EsModel es = new EsModel();
            es.setParms(parms);
            es.setIndex(ConstantUtil.SYS_LOG_INDEX);
            es.setType(ConstantUtil.SYS_LOG_TYPE);
            List<JSONObject> retList = elasticsearchService.search(es);
            int totalCount = 0;
            if (retList != null) {
                totalCount = retList.size();
            }
            List<JSONObject> newList = new ArrayList();
            if (totalCount > 0) {
                for (JSONObject json : retList) {
                    String id = json.getString("id");
                    if (StringUtil.isBlank(id)) {
                        id = StringUtil.getUuId();
                    }
                    json.put("id", id);
                    newList.add(json);
                }
            }
            retJon.put("data", newList);
            retJon.put("recordsTotal", totalCount);
            retJon.put("recordsFiltered", totalCount);
        } catch (Exception e) {
            String msg = "search:(系统日志搜索) 异常====>" + StringUtil.getExceptionMsg(e);
            logger.error(msg);
            sysCommonService.sendDingMessage("search", param.toString(), JSON.toJSONString(retJon), msg, this.getClass());
            e.printStackTrace();
        }
        logger.info("search:(系统日志搜索) 结束");
        return retJon;
    }

}
