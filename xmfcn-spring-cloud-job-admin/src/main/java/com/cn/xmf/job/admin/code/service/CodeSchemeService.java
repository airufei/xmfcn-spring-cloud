package com.cn.xmf.job.admin.code.service;

import java.util.List;
import com.cn.xmf.base.model.Partion;
import com.cn.xmf.job.admin.common.SysCommonService;
import com.cn.xmf.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.cn.xmf.job.admin.code.model.*;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.stereotype.Service;
import com.cn.xmf.job.admin.code.dao.CodeSchemeDao;

/**
 * Service(代码生成方案)
 *
 * @author rufei.cn
 * @version 2018-12-10
 */
@Service
@SuppressWarnings("all")
public class CodeSchemeService {

    @Autowired
    private CodeSchemeDao codeSchemeDao;
    @Autowired
    private CodeSchemeHelperService codeSchemeHelperService;
    @Autowired
    private SysCommonService sysCommonService;

    private static Logger logger = LoggerFactory.getLogger(CodeSchemeService.class);

    /**
     * getList(获取代码生成方案带分页数据-服务)
     *
     * @param json
     * @return
     * @author rufei.cn
     */
    public Partion getList(@RequestBody JSONObject json) {
        logger.info("getList(获取代码生成方案带分页数据-服务) 开始 json={}", json);
        if (json == null || json.size() < 1) {
            return null;
        }
        Partion pt = null;
        try {
            int totalcount = codeSchemeHelperService.getTotalCount(json);
            List<CodeScheme> list = null;
            if (totalcount > 0) {
                list = codeSchemeDao.getList(json);
            }
            pt = new Partion(json, list, totalcount);
        } catch (Exception e) {
            String msg = "getList(获取代码生成方案 异常 " + StringUtil.getExceptionMsg(e);
            logger.error(msg);
            String parms = null;
            if (json != null) {
                parms = json.toString();
            }
            sysCommonService.sendDingTalkMessage("base-service[getList]", parms, null, msg, this.getClass());
        }
        logger.info("getList(获取代码生成方案带分页数据-服务) 结束 ");
        return pt;
    }

    /**
     * getCodeSchemeList(获取代码生成方案 不带分页数据-服务)
     *
     * @param codeScheme
     * @return
     * @author rufei.cn
     */
    public List<CodeScheme> getCodeSchemeList(@RequestBody CodeScheme codeScheme) {
        String parms = JSON.toJSONString(codeScheme);
        List<CodeScheme> list = null;
        logger.info("getCodeSchemeList(获取代码生成方案 不带分页数据-服务) 开始 parms={}", parms);
        if (codeScheme == null) {
            return list;
        }
        try {
            list = codeSchemeDao.getCodeSchemeList(codeScheme);
        } catch (Exception e) {
            String msg = "getCodeSchemeList 异常 " + StringUtil.getExceptionMsg(e);
            logger.error(msg);
            sysCommonService.sendDingTalkMessage("base-service[getCodeSchemeList]", parms, null, msg, this.getClass());

        }
        logger.info("getCodeSchemeList(获取代码生成方案 不带分页数据-服务) 结束");
        return list;
    }


    /**
     * save (保存代码生成方案 数据-服务)
     *
     * @param codeScheme
     * @return
     * @author rufei.cn
     */
    public CodeScheme save(@RequestBody CodeScheme codeScheme) {
        String parms = JSON.toJSONString(codeScheme);
        logger.info("save (保存代码生成方案 数据-服务) 开始 parms={}", parms);
        if (codeScheme == null) {
            return codeScheme;
        }
        try {
            codeScheme = codeSchemeHelperService.save(codeScheme);
        } catch (Exception e) {
            String msg = "save (保存代码生成方案 数据-服务) " + StringUtil.getExceptionMsg(e);
            logger.error(msg);
            sysCommonService.sendDingTalkMessage("base-service[save]", parms, null, msg, this.getClass());

        }
        logger.info("save (保存代码生成方案 数据-服务) 结束");
        return codeScheme;
    }

    /**
     * getCodeScheme(获取代码生成方案单条数据-服务)
     *
     * @param codeScheme
     * @return
     * @author rufei.cn
     */
    public CodeScheme getCodeScheme(@RequestBody CodeScheme codeScheme) {
        CodeScheme ret = null;
        String parms = JSON.toJSONString(codeScheme);
        List<CodeScheme> list = null;
        logger.info("getCodeScheme(获取代码生成方案单条数据-服务) 开始 parms={}", parms);
        if (codeScheme == null) {
            return ret;
        }
        try {
            ret = codeSchemeHelperService.getSignleCodeScheme(codeScheme);
        } catch (Exception e) {
            String msg = "getCodeScheme(获取代码生成方案单条数据-服务) " + StringUtil.getExceptionMsg(e);
            logger.error(msg);
            sysCommonService.sendDingTalkMessage("base-service[getCodeScheme]", parms, null, msg, this.getClass());

        }
        logger.info("getCodeScheme(获取代码生成方案单条数据-服务) 结束 ");
        return ret;
    }

    /**
     * delete(逻辑删除代码生成方案数据-服务)
     *
     * @param id
     * @return
     * @author rufei.cn
     */
    public boolean delete(Long id) {
        logger.info("delete(逻辑删除代码生成方案数据-服务) 开始 id={}", id);
        boolean isSuccess = false;
        if (id < 1) {
            return isSuccess;
        }
        CodeScheme dt = codeSchemeHelperService.getCodeSchemeById(id);
        if (dt == null) {
            return isSuccess;
        }
        codeSchemeDao.delete(id);
        isSuccess = true;
        logger.info("delete(逻辑删除代码生成方案数据-服务)结束 id={},isSuccess={}", id, isSuccess);
        return isSuccess;
    }
}