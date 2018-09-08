package com.cn.xmf.api.log;

import com.alibaba.fastjson.JSONObject;
import com.cn.xmf.api.sys.EsLogService;
import com.cn.xmf.common.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 日志
 */
@RestController
@RequestMapping("/log/")
public class EsLogController {

    private Logger logger = LoggerFactory.getLogger(EsLogController.class);

    @Autowired
    private EsLogService esLogService;

    /**
     * save (保存接口拦截配置 数据-服务)
     *
     * @param parms
     * @return
     */
    @RequestMapping("saveLog")
    public void saveLog(String parms, String index, String type) {
        if (StringUtil.isBlank(parms)) {
            return;
        }
        logger.info("======================进入日志系统=======================");
        esLogService.saveLog(parms, index, type);
    }

    /**
     * getLogById (获取日志)
     *
     * @param id
     * @return
     */
    @RequestMapping("getLogById")
    public JSONObject getLogById(String id, String index, String type) {
        return esLogService.getLogById(id, index, type);
    }
}
