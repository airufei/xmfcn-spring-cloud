package com.cn.xmf.service.es;

import com.alibaba.fastjson.JSONObject;
import com.cn.xmf.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
@RestController
@RequestMapping(value = "/server/log/")
@SuppressWarnings("all")
public class EsLogService {
    private static Logger logger = LoggerFactory.getLogger(EsLogService.class);


    //@Autowired
    private EsDao esDao;

    /**
     * save (保存接口拦截配置 数据-服务)
     *
     * @param interfaceInterceptor
     * @return
     */
    @RequestMapping("saveLog")
    public void saveLog(String parms,String  index,String  type) {
        logger.info("=============================server开始保存日志=============================");
        if (StringUtil.isBlank(parms)) {
            return;
        }
        //JSONObject json= JSONObject.parseObject(parms);
        //esDao.add(json,index,type);
        logger.info("=============================server 保存日志完毕=============================");
    }

    /**
     * getLogById (获取日志)
     *
     * @param interfaceInterceptor
     * @return
     */
    @RequestMapping("getLogById")
    public JSONObject getLogById(String id,String  index,String  type) {
        JSONObject json= null;
        logger.info("=============================开始getLogById (获取日志)=============================");
        if (StringUtil.isBlank(id)) {
            return json;
        }
        if(StringUtil.isBlank(index))
        {
            index="sys_log_index";
        }
        if(StringUtil.isBlank(type))
        {
            type="t_sys_log";
        }
        //json=esDao.getDataByid(index,type,id);
        logger.info("=============================getLogById (获取日志)完毕=============================");
        return json;
    }

}
