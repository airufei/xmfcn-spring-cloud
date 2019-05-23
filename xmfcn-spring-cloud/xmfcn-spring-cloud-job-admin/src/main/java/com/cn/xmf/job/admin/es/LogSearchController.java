package com.cn.xmf.job.admin.es;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cn.xmf.job.admin.common.SysCommonService;
import com.cn.xmf.job.admin.sys.ElasticsearchService;
import com.cn.xmf.model.es.EsModel;
import com.cn.xmf.model.es.EsPartion;
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
 * @Author rufei.cn
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
    @Autowired
    private LogSearchHelperService logSearchHelperService;

    @RequestMapping
    public String index(Model model) {
        String dictType = ConstantUtil.DICT_TYPE_SYS_NAME;
        List<JSONObject> list = logSearchHelperService.getSubSysName(dictType);
        model.addAttribute("sysList", list);
        return "es/log-index";
    }

    /**
     * search:(系统日志搜索)
     *
     * @param request
     * @return
     * @Author rufei.cn
     */
    @RequestMapping("search")
    @ResponseBody
    public JSONObject search(HttpServletRequest request) {
        JSONObject retJon = new JSONObject();
        retJon.put("data", "");
        retJon.put("recordsTotal", 0);
        retJon.put("recordsFiltered", 0);
        JSONObject param = null;
        try {
            EsModel parms = logSearchHelperService.getParms(request);
            EsPartion pt = elasticsearchService.search(parms);
            if (pt == null) {
                return retJon;
            }
            int totalCount = pt.getTotalCount();
            List<JSONObject> retList = pt.getList();
            retJon.put("data", retList);
            retJon.put("recordsTotal", totalCount);
            retJon.put("recordsFiltered", totalCount);
        } catch (Exception e) {
            String msg = "search:(系统日志搜索) 异常====>" + StringUtil.getExceptionMsg(e);
            logger.error(msg);
            sysCommonService.sendDingMessage("search", param.toString(), JSON.toJSONString(retJon), msg, this.getClass());
            e.printStackTrace();
        }
        logger.info("search:(系统日志搜索) ==结束==");
        return retJon;
    }

}
