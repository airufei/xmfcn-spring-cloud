package com.cn.xmf.job.admin.code.controller;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.cn.xmf.base.model.Partion;
import com.cn.xmf.base.model.ResultCodeMessage;
import com.cn.xmf.job.admin.common.SysCommonService;
import com.cn.xmf.job.core.biz.model.ReturnT;
import com.cn.xmf.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSON;
import com.cn.xmf.job.admin.code.model.*;
import com.cn.xmf.job.admin.code.service.*;

/**
 * CodeSchemeController(代码生成方案)
 *
 * @author rufei.cn
 * @version 2018-12-10
 */
@Controller
@RequestMapping("/codeScheme")
@SuppressWarnings("all")
public class CodeSchemeController {

    private static Logger logger = LoggerFactory.getLogger(CodeSchemeService.class);
    @Autowired
    private CodeSchemeService codeSchemeService;
    @Autowired
    private SysCommonService sysCommonService;

    @RequestMapping
    public String index() {
        return "code/codeScheme-index";
    }

    /**
     * getList:(获取代码生成方案分页查询接口)
     *
     * @param request
     * @return
     * @author rufei.cn
     */
    @RequestMapping("pageList")
    @ResponseBody
    public JSONObject getList(HttpServletRequest request) {
        JSONObject retJon = new JSONObject();
        JSONObject param = null;
        try {
            String startStr = request.getParameter("start");
            String length = request.getParameter("length");
            String name = request.getParameter("name");
            String tableName = request.getParameter("tableName");
            int pageSize = 10;
            int pageNo = 1;
            int start = 0;
            if (StringUtil.isNotBlank(startStr)) {
                start = StringUtil.stringToInt(startStr);
            }
            if (StringUtil.isNotBlank(length)) {
                pageSize = StringUtil.stringToInt(length);
            }
            if (start > 0) {
                pageNo = (start / pageSize) + 1;
            }
            param = StringUtil.getPageJSONObject(pageNo, pageSize);
            logger.info("getList:(获取代码生成方案分页查询接口) 开始  param={}", param);
            param.put("name", name);
            param.put("tableName", tableName);
            Partion pt = codeSchemeService.getList(param);
            List<CodeScheme> list = null;
            int totalCount = 0;
            if (pt != null) {
                list = (List<CodeScheme>) pt.getList();
                totalCount = pt.getTotalCount();
            }
            retJon.put("data", list);
            retJon.put("recordsTotal", totalCount);
            retJon.put("recordsFiltered", totalCount);
        } catch (Exception e) {
            String msg = "getList:(获取代码生成方案分页查询接口) 异常====>" + StringUtil.getExceptionMsg(e);
            logger.error(msg);
            sysCommonService.sendDingTalkMessage("getList", param.toString(), JSON.toJSONString(retJon), msg, this.getClass());

        }
        logger.info("getList:(获取代码生成方案分页查询接口) 结束");
        return retJon;
    }

    /**
     * delete:(逻辑删除代码生成方案数据接口)
     *
     * @param request
     * @param parms
     * @return
     * @author rufei.cn
     */
    @RequestMapping("delete")
    @ResponseBody
    public ReturnT<String> delete(HttpServletRequest request) {
        ReturnT<String> retData = new ReturnT<>(ResultCodeMessage.FAILURE, "删除失败");
        String ids = null;
        try {
            ids = request.getParameter("id");
            int id = StringUtil.stringToInt(ids);
            logger.info("delete 开始============>" + id);
            if (id <= 0) {
                retData.setMsg(ResultCodeMessage.PARMS_ERROR_MESSAGE);
                return retData;
            }
            long newId = id;
            boolean ret = codeSchemeService.delete(newId);
            if (ret) {
                retData.setCode(ResultCodeMessage.SUCCESS);
                retData.setMsg(ResultCodeMessage.SUCCESS_MESSAGE);
            }
        } catch (Exception e) {
            retData.setCode(ResultCodeMessage.FAILURE);
            retData.setMsg(ResultCodeMessage.EXCEPTION_MESSAGE);
            String msg = "delete:(逻辑删除代码生成方案数据接口) error===>" + StringUtil.getExceptionMsg(e);
            logger.error(msg);
            sysCommonService.sendDingTalkMessage("delete", ids, null, msg, this.getClass());
        }
        logger.info("delete 结束============>" + JSON.toJSONString(retData));
        return retData;
    }

    /**
     * save:(保存代码生成方案数据接口)
     *
     * @param request
     * @param parms
     * @return
     * @author rufei.cn
     */
    @RequestMapping(value = "save")
    @ResponseBody
    public ReturnT<String> save(CodeScheme codeScheme) {
        ReturnT<String> retData = new ReturnT<>(ResultCodeMessage.FAILURE, "保存数据失败");
        String parms = null;
        try {
            parms = JSON.toJSONString(codeScheme);
            logger.info("save:(保存代码生成方案数据接口) 开始  parms={}", parms);
            if (codeScheme == null) {
                retData.setMsg(ResultCodeMessage.PARMS_ERROR_MESSAGE);
                return retData;
            }
            if (codeScheme == null) {
                retData.setMsg(ResultCodeMessage.PARMS_ERROR_MESSAGE);
                return retData;
            }
            CodeScheme ret = codeSchemeService.save(codeScheme);
            if (ret == null) {
                retData.setMsg("保存数据失败");
                return retData;
            }
            retData.setCode(ResultCodeMessage.SUCCESS);
            retData.setMsg(ResultCodeMessage.SUCCESS_MESSAGE);
        } catch (Exception e) {

            String msg = "save:(保存代码生成方案数据接口) error===>" + StringUtil.getExceptionMsg(e);
            logger.error(msg);
            sysCommonService.sendDingTalkMessage("save", parms, JSON.toJSONString(retData), msg, this.getClass());
            retData.setMsg(ResultCodeMessage.EXCEPTION_MESSAGE);
        }
        logger.info("save 结束============>" + JSON.toJSONString(retData));
        return retData;
    }

}