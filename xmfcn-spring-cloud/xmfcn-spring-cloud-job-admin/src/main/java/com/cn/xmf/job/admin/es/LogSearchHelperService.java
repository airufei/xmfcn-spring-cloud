package com.cn.xmf.job.admin.es;

import com.alibaba.fastjson.JSONObject;
import com.cn.xmf.job.admin.sys.DictService;
import com.cn.xmf.model.dict.Dict;
import com.cn.xmf.model.es.EsModel;
import com.cn.xmf.model.es.EsPage;
import com.cn.xmf.model.es.LogMessage;
import com.cn.xmf.util.ConstantUtil;
import com.cn.xmf.util.DateUtil;
import com.cn.xmf.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * 日志搜索辅助类
 *
 * @version 2019-05-23
 * @Author rufei.cn
 */
@Service
@SuppressWarnings("all")
public class LogSearchHelperService {

    private static Logger logger = LoggerFactory.getLogger(LogSearchHelperService.class);
    @Autowired
    private DictService dictService;

    /**
     * 搜索装配参数
     *
     * @param request
     * @return
     */
    public EsModel getParms(HttpServletRequest request) {
        String subSysName = request.getParameter("subSysName");
        String message = request.getParameter("message");
        String traceId = request.getParameter("traceId");
        String stackMessage = request.getParameter("stackMessage");
        String methodName = request.getParameter("methodName");
        String level = request.getParameter("level");
        String startStr = request.getParameter("start");
        String length = request.getParameter("length");
        int pageSize = 10;
        int pageNo = 1;
        int startIndex = 0;
        if (StringUtil.isNotBlank(startStr)) {
            startIndex = StringUtil.stringToInt(startStr);
        }
        if (StringUtil.isNotBlank(length)) {
            pageSize = StringUtil.stringToInt(length);
        }
        if (startIndex > 0) {
            pageNo = (startIndex / pageSize) + 1;
        }
        JSONObject highlights = new JSONObject();
        highlights.put("message", message);
        JSONObject searchKey = new JSONObject();
        searchKey.put("subSysName", subSysName);
        searchKey.put("message", message);
        searchKey.put("traceId", traceId);
        searchKey.put("stackMessage", stackMessage);
        searchKey.put("methodName", methodName);
        searchKey.put("level", level);
        JSONObject keywords = new JSONObject();
        EsPage esPage = new EsPage();
        esPage.setPageNo(pageNo);
        esPage.setPageSize(pageSize);
        esPage.setStartIndex(startIndex);
        EsModel es = new EsModel();
        es.setKeyWord(searchKey);
        es.setHightWord(highlights);
        es.setIndex(ConstantUtil.ES_SYS_LOG_INDEX);
        es.setType(ConstantUtil.ES_SYS_LOG_TYPE);
        es.setEsPage(esPage);
        return es;
    }


    /**
     * 获取子系统名称
     *
     * @param dictType 系统类型
     * @return
     */
    public List<JSONObject> getSubSysName(String dictType) {
        List<JSONObject> list = null;
        if (StringUtil.isBlank(dictType)) {
            return list;
        }
        Dict dict = new Dict();
        dict.setType(dictType);
        List<Dict> dictList = dictService.getDictList(dict);
        if (dictList == null || dictList.size() <= 0) {
            return list;
        }
        list = new ArrayList<>();
        for (Dict item : dictList) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("subSysName", item.getDictValue());
            list.add(jsonObject);
        }
        return list;
    }

    /**
     * 搜索装配参数
     *
     * @param request
     * @return
     */
    public EsModel getStatisticsParms(HttpServletRequest request) {
        String subSysName = request.getParameter("subSysName");
        String startTime = request.getParameter("startTime");
        String endTime = request.getParameter("endTime");
        startTime = DateUtil.toDateStr(startTime, "yyyy-MM-dd");
        endTime = DateUtil.toDateStr(endTime, "yyyy-MM-dd");
        EsModel es = new EsModel();
        es.setStartTime(startTime);
        es.setEndTime(endTime);
        es.setIndex(ConstantUtil.ES_SYS_LOG_INDEX);
        es.setType(ConstantUtil.ES_SYS_LOG_TYPE);
        return es;
    }
}
